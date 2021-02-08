package proxy;

public class Demo {
    public static void main(String[] args) {
        TestLoggingInterface testLogging = (TestLoggingInterface) Ioc.createMyClass(new TestLogging(), TestLoggingInterface.class);
        testLogging.calculation(4);

        Demo demo = new Demo();
        demo.action();
    }

    public void action() {
        new TestLogging().build().calculation(1);
    }
}



