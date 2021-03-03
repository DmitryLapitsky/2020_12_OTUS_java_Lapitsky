package atm;

public class Money implements MoneyInterface{

    private int numberOfBills;

    /**
     * загрузка количества банкнот (не сумма рублей)
     *
     * @param numberOfBills количетво банкнот
     */
    public Money(int numberOfBills) {
        this.numberOfBills = numberOfBills;
        if (getType() == -1) {
            throw new RuntimeException("инициализация валюты не прошла");
        }
    }

    /**
     * возвращает номинал банкнот текущего класса по его названию или возвращает ошибку), если название класса неверно
     *
     * @return номинал банкнот в формате double (вдруг понадобятся копейки)
     */
    public double getType() {
        for (MoneyTypes moneyType : MoneyTypes.values()) {
            if (this.getClass().getSimpleName().equals(moneyType.name())) {
                return moneyType.getValue();
            }
        }
        throw new RuntimeException("название класса " + this.getClass().getSimpleName() + " неверно");
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
