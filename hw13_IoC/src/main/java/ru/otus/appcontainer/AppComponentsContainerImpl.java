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
        Map<Integer, Map<String, Method>> orders = new TreeMap<>();
        //раскладываем методы по индексам/orders
        for (Method method : configClass.getMethods()) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                int order = method.getAnnotation(AppComponent.class).order();
                if (orders.containsKey(order)) {
                    orders.get(order).put(method.getName(), method);
                } else {
                    orders.put(order, new HashMap<>() {{
                        put(method.getName(), method);
                    }});
                }
            }
        }
        //поочередно запускаем методы, передавая в них необходимые параметры
        try {
            for (Integer order : orders.keySet()) {
                for (Map.Entry<String, Method> el : orders.get(order).entrySet()) {
                    Method method = el.getValue();
                    if (contains(method.getParameterTypes(), appComponentsByName.keySet())) {
                        Class<?>[] argNeed = method.getParameterTypes();
                        Object[] methodArgs = new Object[argNeed.length];
                        for (int i = 0; i < methodArgs.length; i++) {
                            methodArgs[i] = appComponentsByName.get(argNeed[i].getSimpleName().toLowerCase());
                        }
                        appComponents.add(method.invoke(configClass.getConstructor().newInstance(), methodArgs));
                        appComponentsByName.put(method.getName().toLowerCase(), appComponents.get(appComponents.size() - 1));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error " + e);
        }
    }

    /**
     * Проверяет, есть ли все необходимые экземпляры классов, чтобы передать их как параметры в метод
     * @param parameters параметры метода
     * @param instances существующие объекты
     * @return возвращает true, если для метода есть все объекты, false - если нет
     */
    private boolean contains(Class<?>[] parameters, Set<String> instances) {
        boolean contains = true;
        for (Class<?> param : parameters) {
            for (String ini : instances) {
                if (param.toString().toLowerCase().contains(ini.toLowerCase())) {
                    contains = true;
                    break;
                } else {
                    contains = false;
                }
            }
        }
        return contains;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.get(appComponents.size() - 1);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponents.get(appComponents.size() - 1);
    }
}