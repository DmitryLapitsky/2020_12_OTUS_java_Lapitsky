package testFrameWork;

import java.lang.reflect.*;
import java.util.*;

public class TestFrameWork {

    private boolean testTotal = true;
    private Map<String, Map<Boolean, String>> classResult = new HashMap<>();//классы, методы(результаты, ошибки)
    private String className;

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("class: ").append(className).append(" test: ").append(testTotal).append("\n");
        int methodsCount = classResult.size();
        int failedTests = 0;
        out.append("\ttested methods:").append(classResult.keySet()).append("\n");
        for (Map.Entry<String, Map<Boolean, String>> method : classResult.entrySet()) {
            for (Map.Entry<Boolean, String> methodRes : method.getValue().entrySet()) {
                if (methodRes.getKey()) {
                    out.append(method.getKey()).append(" -> ").append(true).append("\n");
                } else {
                    out.append(method.getKey()).append(" -> ").append(false).append("\tCause:").append(methodRes.getValue()).append("\n");
                    failedTests++;
                }
            }
        }
        out.append("\n\ttested methods:").append(methodsCount).append(" passed:").append(methodsCount - failedTests).append(" failed:").append(failedTests).append("\n");
        return out.toString();
    }

    public int getFailed() {
        int failedTests = 0;
        for (Map.Entry<String, Map<Boolean, String>> method : classResult.entrySet()) {
            for (Map.Entry<Boolean, String> methodRes : method.getValue().entrySet()) {
                if (!methodRes.getKey()) {
                    failedTests++;
                }
            }
        }
        return failedTests;
    }

    public int getTestsCount() {
        return classResult.size();
    }

    public static void main(String[] args) throws ClassNotFoundException {
        TestFrameWork tf = new TestFrameWork();
        tf.run("testFrameWork.DemoClass");
        System.out.println(tf.toString());
    }

    public TestFrameWork run(String className) throws ClassNotFoundException {
        this.className = className;
        Class<?> classTested = Class.forName(className);
        System.out.println(classTested.getTypeName());
        Method[] testedClassMethods = classTested.getDeclaredMethods();
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        for (Method method : testedClassMethods) {
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
            if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            }
        }
        Collections.shuffle(testMethods);           //тесты каждый раз будут в случайном порядке

        for (Method testMethod : testMethods) {     //проходим по всем методам с меткой @Test
            Object testedClass = instantiate(classTested);//создаем отдельный экземпляр тестируемого класса
            System.out.println("***** Testing {" + testMethod.getName() + "} *****");
            boolean methodTestPass = true;              //
            String beforeTempMethod = null;
            String methodTestError = "no error";
            try {                                   //в одном блоке вначале методы before, затем test, т.к. какая разница, что сломается
                for (Method beforeMethod : beforeMethods) { //подготовительных методов может быть несколько
                    beforeTempMethod = beforeMethod.getName();
                    callMethod(testedClass, beforeMethod);
                }
                try {
                    callMethod(testedClass, testMethod);
                } catch (Exception e) {
                    System.out.println("Test Method Exception in " + testMethod.getName() + " " + e.getCause());
                    methodTestError = "Test Method Exception in " + testMethod.getName() + " " + e.getCause();
                    methodTestPass = false;
                }
            } catch (Exception e) {
                System.out.println("Before Method Exception in " + beforeTempMethod + " " + e.getCause());
                methodTestError = "Before Method Exception in " + beforeTempMethod + " " + e.getCause();
                methodTestPass = false;
            } finally {                             //выполнить всегда метод after
                for (Method afterMethod : afterMethods) {
                    try {
                        callMethod(testedClass, afterMethod);
                    } catch (Exception e) {
                        System.out.println("After Method Exception in " + afterMethod.getName() + " " + e.getCause());
                        methodTestError = "After Method Exception in " + beforeTempMethod + " " + e.getCause();
                        methodTestPass = false;         //т.к. тест все равно не пройден
                    }
                }
                if (testTotal) {
                    testTotal = methodTestPass;
                }
            }
            System.out.println(methodTestPass + " *****\n");

            Map<Boolean, String> methodTestResult = new HashMap<>();
            methodTestResult.put(methodTestPass, methodTestError);
            classResult.put(testMethod.getName(), methodTestResult);
        }
        return this;
    }

    public static void callMethod(Object object, Method method) throws InvocationTargetException, IllegalAccessException {//вызов метода, предварительно изменив его видимость
        method.setAccessible(true);
        method.invoke(object);
    }

    /**
     * инициализирует экземпляр класса по типу и передаваемым аргументам. Если аргумены с параметрами конструктора класса не совпадут, то ошибка с выводом возможных конструкторов класса
     *
     * @param type класс вызываемого объекта
     * @param <T>  тип возвращаемого класса
     * @return экземляр класса
     */
    public static <T> T instantiate(Class<T> type) {    //вызов эземпляра класса, предварительно изменив его видимость
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("!!!!!!" + type.getTypeName());
            throw new RuntimeException("Failed to load class: " + type.getName() + ". Cause: " + e.toString());
        }
    }
}