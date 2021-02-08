package proxy;


public class TestLogging implements TestLoggingInterface {

    public TestLoggingInterface build(){
        return (TestLoggingInterface) Ioc.createMyClass(this, TestLoggingInterface.class);
    }

    @Log
    public void calculation(int param) {
        System.out.println("-> " + param);
    }

    public void calculation(int param, int param2) {
        System.out.println(param + param2);
    }
}
