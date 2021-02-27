package atm;

public abstract class Money {

    private int numberOfBills;

    //список доступных монет различного номинала: 5000р-1р и 50-1 коп (0,5 - 0,01)
    private static double[] moneyTypes = new double[]{5000,1000,500,100,50,10,5,2,1,0.5,0.1,0.05,0.01};

    /**
     * загрузка количества банкнот (не сумма рублей)
     * @param numberOfBills количетво банкнот
     */
    public Money(int numberOfBills){
        this.numberOfBills = numberOfBills;
        if(getType() == -1){
            throw new RuntimeException("инициализация валюты не прошла");
        }
    }

    /**
     * возвращает номинал банкнот текущего класса по его названию или возвращает -1 (или ошибку), если название класса неверно
     * @return номинал банкнот в формате double (вдруг понадобятся копейки)
     */
    public double getType() {
        try {
            double classType;
            if(!this.getClass().getSimpleName().startsWith("Cash")){
                throw new RuntimeException("название класса " + this.getClass().getSimpleName() + " неверно");
            }
            String typeString = this.getClass().getSimpleName().split("Cash")[1];
            if(typeString.startsWith("0")){//для копеек
                classType = Double.parseDouble("0." + typeString.substring(1));
            }
            else{
                classType = Double.parseDouble(typeString);
            }
            if(!checkType(classType)){
                throw new RuntimeException("название класса " + this.getClass().getSimpleName() + " неверно");
            }
            return classType;
        }catch (RuntimeException e){
            System.out.println("wrong cash value initialization " + e.toString());
        }
        return -1;
    }

    /**
     * сверяет номинал из названия класса со списком номиналов из массива moneyTypes
     * @param classType номинал банкноты
     * @return если номинал содержится в массиве, то все ок
     */
    private boolean checkType(double classType){
        for(double type : moneyTypes){
            if(classType == type){
                return true;
            }
        }
        return false;
    }

    /**
     * снятие количества банкнот, если количество запрашиваемых банкнот меньше или равно текущему запасу
     * с уменьшением запаса на такое же количетво наличности
     * @param numberOfBills запрашиваемое количество банкнот
     * @return либо количетво запрашиваемых банкнот либо 0, если запрашивалось больше
     */
    public int getAmount(int numberOfBills) {
        if(numberOfBills<=this.numberOfBills){
            this.numberOfBills -= numberOfBills;
            return numberOfBills;
        }
        else{
            return 0;
        }
    }

    /**
     * показывает количетво запрашиваемых банкнот
     * @return количетво запрашиваемых банкнот
     */
    public int showAvailable(){
        return numberOfBills;
    }

    /**
     * показывает количетво денег данного номинала в валюте
     * @return количетво денег данного номинала
     */
    public double showAvailableMoney(){
        return numberOfBills*getType();
    }

    /**
     * аналог showAvailable для упрощения логгирования и использования в в методе println
     * @return текстовое предстваление количетва запрашиваемых банкнот
     */
    public String toString(){
        return String.valueOf(numberOfBills);
    }
}
