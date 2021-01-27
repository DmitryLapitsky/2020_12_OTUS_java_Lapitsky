public class TestAssert {

    static public void checkTrue(Object expected, Object current, String errorMessage) {
        int c = current == null ? 1 : 0;
        int e = expected == null ? 1 : 0;
        if (c + e == 1) {
            System.out.println("\tError 1\n\tExpected " + expected + "\n\tCurrent " + current + "\n\t" + errorMessage);
            throw new Error();
        } else if (current != null && !current.equals(expected)) {
            System.out.println("\tError 2\n\tExpected " + expected + "\n\tCurrent " + current + "\n\t" + errorMessage);
            throw new Error();
        }


    }
}
