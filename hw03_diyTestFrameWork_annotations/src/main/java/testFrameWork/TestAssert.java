package testFrameWork;

public class TestAssert {

    static public void checkTrue(Object expected, Object current, String errorMessage) {
        int c = current == null ? 1 : 0;
        int e = expected == null ? 1 : 0;
        if (c + e == 1) {
            throw new AssertException( expected,  current,  errorMessage);
        } else if (current != null && !current.equals(expected)) {
            throw new AssertException(expected, current, errorMessage);
        }
    }

    static class AssertException extends RuntimeException {

        public AssertException(Object expected, Object current, String errorMessage) {
            String error = "\tTest error\n\tExpected " + expected + "\n\tCurrent " + current + "\n\t" + errorMessage;
            System.out.println(error);
        }
    }
}
