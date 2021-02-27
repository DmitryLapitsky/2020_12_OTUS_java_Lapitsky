package atm;

public class ATM_DEMO {

    public static void main(String[] args) {
        script(new ATM(1, 0, 110, 10, 10, 20), 12570);
        script(new ATM(1, 0, 10, 10, 10, 0), 12571);
        script(new ATM(1, 0, 110, 10, 10, 0), 125710000);
        script(new ATM(1, 0, 110, 10, 10, 0), -1);
    }

    public static void script(ATM atm, long withdrawal){
        System.out.println("в банкомате сумма " + atm.summ_left());
        System.out.println("запрос на снятие " + withdrawal);
        try {
            System.out.println("снятие " + atm.withdrawal(withdrawal));
        } catch (RuntimeException e) {
            System.out.println(e);
        }
        System.out.println("в банкомате осталось " + atm.summ_left());
        System.out.println("в банкомате осталось " + atm.left());
        System.out.println();
    }
}


