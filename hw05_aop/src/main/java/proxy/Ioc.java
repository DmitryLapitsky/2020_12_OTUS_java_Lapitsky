package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

class Ioc {

    private Ioc() {
    }

    static Object createMyClass(Object object, Class T) {
        return Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{T}, new DemoInvocationHandler(object));
    }


    static class DemoInvocationHandler implements InvocationHandler {

        private final Object myClass;

        DemoInvocationHandler(Object proxyClass) {
            this.myClass = proxyClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Class<?> objectClass = myClass.getClass();
            Method[] testedClassMethods = objectClass.getDeclaredMethods();
            for (Method realMethod : testedClassMethods) {

                if (realMethod.isAnnotationPresent(Log.class) && Arrays.toString(method.getParameters()).equals(Arrays.toString(realMethod.getParameters()))){
                    if (args.length == 0) {
                        System.out.println("executed method: " + realMethod.getName());
                    }
                    else if (args.length == 1) {
                        System.out.println("executed method: " + realMethod.getName() + ", param: " + args[0]);
                    }
                    else {
                        System.out.println("executed method: " + realMethod.getName() + ", param: " + Arrays.toString(args));
                    }
                }
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
