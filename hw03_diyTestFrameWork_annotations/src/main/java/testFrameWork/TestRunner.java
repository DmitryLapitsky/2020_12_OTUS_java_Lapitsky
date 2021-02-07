package testFrameWork;

import java.util.Arrays;

public class TestRunner {

    public static void main(String[] args)  {
        System.out.println("test " + Arrays.toString(args));
        for(String className : args){
            try {
                System.out.println("->" + className);
                TestFrameWork testFrameWork = new TestFrameWork();
                System.out.println(testFrameWork.getTestsCount());
                TestFrameWork classResult = testFrameWork.run(className);
                System.out.println(classResult);

                System.out.println(testFrameWork.getFailed());
                System.out.println(testFrameWork.getTestsCount());
            }catch (Exception e){
                System.out.println(className + " test was not initiated. Cause: " + e.toString());
            }
        }
    }

}
