public class DemoClass {

    public DemoClass(String test2Value, int test3int) {
        this.test2Value = test2Value;
        this.test3int = test3int;
    }

    public DemoClass(){

    }

    public Integer test3int = 3;
    String test2Value = "1";
    public String test1Value = "no";

    @Before
    public void beforeMethod() {
        System.out.println("beforeMethod set test1Value = yes");
        test1Value = "yes";
    }

    @Test
    private void test1_WordYesFromBefore() {
        System.out.println("test 1 word is yes (now is'" + test1Value + "')");
        TestAssert.checkTrue("yes", test1Value, "не сошёлся ответ");
    }

    @Test
    public void test2_test2Value_Is_2() {
        System.out.println("test_2 word is 2 (now is'" + test2Value + "')");
        TestAssert.checkTrue("2", test2Value, "не сошёлся ответ");
    }

    @Test
    public void test3_divideBy0() {
        System.out.println("test 3 and value divider is " + test3int);
        int error = 0 / test3int;
        TestAssert.checkTrue(0, error, "не сошёлся ответ");
    }

    @After
    public void afterMethod() {
        System.out.println("afterMethod");
    }
}
