import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class TestFrameWork {

    static Object[] constructorArgs;
    String className;

    //два конструктора тестера - с аргументами и без
    public TestFrameWork(String className, Object... args) {
        this.className = className;
        constructorArgs = args;
    }

    public TestFrameWork(String className) {
        this.className = className;
        constructorArgs = new Object[]{};
    }

    public static void main(String[] args) throws ClassNotFoundException {
        TestFrameWork diy = new TestFrameWork("DemoClass", "2", 0);
        Class<?> tester = Class.forName(diy.className);

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
            Object testedClass = instantiate(tester, constructorArgs);//создаем отдельный экземпляр тестируемого класса
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
        System.out.println("Class test result: " + classTestTotalResult + ". Passed " + (testMethods.size() - failedTests) + " from " + testMethods.size());
    }

    public static void callMethod(Object object, Method method) throws InvocationTargetException, IllegalAccessException {//вызов метода, предварительно изменив его видимость
        method.setAccessible(true);
        method.invoke(object);
    }

    /**
     * инициализирует экземпляр класса по типу и передаваемым аргументам. Если аргумены с параметрами конструктора класса не совпадут, то ошибка с выводом возможных конструкторов класса
     * @param type класс вызываемого объекта
     * @param args аргументы вызова, которые будут сравниваться с параметрами конструктора класса
     * @param <T> тип возвращаемого класса
     * @return экземляр класса
     */
    public static <T> T instantiate(Class<T> type, Object... args) {    //вызов эземпляра класса, предварительно изменив его видимость
        try {
            Class<?>[] classes = new Class[args.length];
            Arrays.fill(classes, null);

            Constructor<?>[] constructors = type.getConstructors();
            for (Constructor<?> constructor : constructors) {   //для каждого конструктора
                constructor.setAccessible(true);
                if (constructor.getParameterTypes().length == args.length) {//если у конструктора несколько параметров и они их количество такое же, как у передаваемых
                    if (args.length > 0) {//если передано несколько аргументов, то в конструкторах ищем совпадения и заполняем Class<?>[] classes
                        for (int i = 0; i < args.length; i++) {
                            Class<?> classElement = constructor.getParameterTypes()[i];
                            if (args[i].getClass().toString().toLowerCase().contains(classElement.getTypeName().toLowerCase())) { //пробовал args[i].getClass().isInstance(classElement), но не сработало, использовал текстовое представление классов
                                classes[i] = classElement;
                            } else {//если несовпадение, то обнуляем массив типов и все заново
                                Arrays.fill(classes, null);
                                break;
                            }
                        }
                        if (classes[args.length - 1] != null) {//если последний элемент заполнен, то все хорошо
                            return type.getDeclaredConstructor(classes).newInstance(args);
                        }
                    } else { //если количество передаваемых аргументов параметров конструктора = 0
                        return type.getDeclaredConstructor().newInstance();
                    }
                }
            }
            throw new RuntimeException("No sutable construction for args " + Arrays.toString(args) + ". Available constructors " + Arrays.toString(constructors));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}