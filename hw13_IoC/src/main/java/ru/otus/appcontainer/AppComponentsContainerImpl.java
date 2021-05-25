package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        // You code here...
        List<Method> methods = new ArrayList<>();
        //раскладываем методы по индексам/orders
        for (Method method : configClass.getMethods()) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                methods.add(method);
            }
        }
        methods.sort(Comparator.comparingInt(o -> o.getAnnotation(AppComponent.class).order()));
        //поочередно запускаем методы, передавая в них необходимые параметры
        try {
            for (Method method : methods) {
                Class<?>[] argNeed = method.getParameterTypes();
                Object[] methodArgs = new Object[argNeed.length];
                for (int i = 0; i < methodArgs.length; i++) {
                    for (Object ini : appComponents) {
                        if (argNeed[i].isAssignableFrom(ini.getClass())) {
                            methodArgs[i] = ini;
                        }
                    }
                }
                for (Object o : methodArgs) {
                    if(o == null){
                        throw new RuntimeException("no objects for method " + method.getName());
                    }
                }
                appComponents.add(method.invoke(configClass.getConstructor().newInstance(), methodArgs));
                appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), appComponents.get(appComponents.size() - 1));
            }
        } catch (Exception e) {
            throw new RuntimeException("Application start error " + e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object component : appComponents) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                return (C) component;
            }
        }
        throw new RuntimeException("no corresponding class " + componentClass.getName() + " in " + appComponents);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        }
        throw new RuntimeException("no corresponding class " + componentName + " in " + appComponentsByName);
    }
}