package atm;

public class Money implements MoneyInterface{

    private int numberOfBills;

    private MoneyTypes type;

    /**
     * загрузка номинала банкнот и их количества (не сумма денег)
     *
     * @param numberOfBills количетво банкнот
     */
    public Money(MoneyTypes moneyType, int numberOfBills) {
        this.numberOfBills = numberOfBills;
        type = moneyType;
    }

    /**
     * возвращает номинал банкнот текущего класса
     *
     * @return номинал банкнот в формате double (вдруг понадобятся копейки)
     */
    public double getType() {
        return type.getValue();
    }

    /**
     * снятие количества банкнот, если количество запрашиваемых банкнот меньше или равно текущему запасу
     * с уменьшением запаса на такое же количетво наличности
     *
     * @param numberOfBills запрашиваемое количество банкнот
     * @return либо количетво запрашиваемых банкнот либо 0, если запрашивалось больше
     */
    public int getAmount(int numberOfBills) {
        if (numberOfBills <= this.numberOfBills) {
            this.numberOfBills -= numberOfBills;
            return numberOfBills;
        } else {
            return 0;
        }
    }

    /**
     * показывает количетво запрашиваемых банкнот
     *
     * @return количетво запрашиваемых банкнот
     */
    public int showAvailable() {
        return numberOfBills;
    }

    /**
     * показывает количетво денег данного номинала в валюте
     *
     * @return количетво денег данного номинала
     */
    public double showAvailableMoney() {
        return numberOfBills * getType();
    }

    /**
     * аналог showAvailable для упрощения логгирования и использования в в методе println
     *
     * @return текстовое предстваление количетва запрашиваемых банкнот
     */
    public String toString() {
        return String.valueOf(numberOfBills);
    }
}
