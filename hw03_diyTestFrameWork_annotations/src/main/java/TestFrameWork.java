import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class TestFrameWork {

    public static void main(String[] args)  {
        System.out.println("test " + Arrays.toString(args));
        for(String classNmae : args){
            try {
                System.out.println("->" + classNmae);
                TestFrameWork testFrameWork = new TestFrameWork();
                String result = testFrameWork.run(classNmae);
                System.out.println(result);
            }catch (Exception e){
                System.out.println(classNmae + " test was not initiated. Cause: " + e.toString());
            }
        }
    }


    public String run(String className) throws ClassNotFoundException {
        Class<?> tester = Class.forName(className);
        System.out.println(tester.getTypeName());
        Method[] testedClassMethods = tester.getDeclaredMethods();
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

        boolean classTestTotalResult = true;        //общий результат тестирования класса
        int failedTests = 0;
        for (Method testMethod : testMethods) {     //проходим по всем методам с меткой @Test
            Object testedClass = instantiate(tester);//создаем отдельный экземпляр тестируемого класса
            System.out.println("***** Testing {" + testMethod.getName() + "} *****");
            boolean testPass = true;              //
            String beforeTempMethod = null;
            try {                                   //в одном блоке вначале методы before, затем test, т.к. какая разница, что сломается
                for (Method beforeMethod : beforeMethods) { //подготовительных методов может быть несколько
                    beforeTempMethod = beforeMethod.getName();
                    callMethod(testedClass, beforeMethod);
                }
                try {
                    callMethod(testedClass, testMethod);
                } catch (Exception e) {
                    System.out.println("Test Method Exception in " + testMethod.getName() + " " + e.getCause());
                    testPass = false;
                }
            } catch (Exception e) {
                System.out.println("Before Method Exception in " + beforeTempMethod + " " + e.getCause());
                testPass = false;
            } finally {                             //выполнить всегда метод after
                for (Method afterMethod : afterMethods) {
                    try {
                        callMethod(testedClass, afterMethod);
                    } catch (Exception e) {
                        System.out.println("After Method Exception in " + afterMethod.getName() + " " + e.getCause());
                        testPass = false;         //т.к.
                    }
                }
                if (classTestTotalResult) {
                    classTestTotalResult = testPass;
                }
            }
            if (!testPass)
                failedTests++;
            System.out.println(testPass + " *****\n");
        }
        return tester.getName() + " test result: " + classTestTotalResult + ". Passed " + (testMethods.size() - failedTests) + " from " + testMethods.size();
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
            System.out.println("!!!!!!"+type.getTypeName());
            throw new RuntimeException("Failed to load class: " + type.getName() + ". Cause: " + e.toString());
        }
    }
}