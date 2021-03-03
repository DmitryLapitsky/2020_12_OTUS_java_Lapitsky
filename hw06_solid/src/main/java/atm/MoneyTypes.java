package atm;
public enum MoneyTypes {
    Cash5000(5000),
    Cash1000(1000),
    Cash500(500),
    Cash100(100),
    Cash50(50),
    Cash10(10),
    Cash5(5),
    Cash2(2),
    Cash1(1),
    Cash05(0.5),
    Cash01(0.1),
    Cash005(0.05),
    Cash001(0.01);
    private double amount;

    /**
     * конструктор класса, присваивает каждому элементу перечисления определенную сумму
     * @param amount сумма номинала валюты
     */
    MoneyTypes(double amount) {
        this.amount = amount;
    }

    /**
     * получает номинал валюты
     * @return номинал валюты
     */
    public double getValue(){
        return amount;
    }
}
