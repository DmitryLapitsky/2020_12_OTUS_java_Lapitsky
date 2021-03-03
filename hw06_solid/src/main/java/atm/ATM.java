package atm;

import java.util.ArrayList;
import java.util.List;

public class ATM {

    //массив количетва банкнот
    private List<Money> atm = new ArrayList<>();

    /**
     * загрузка банкнот в банкомат (только бумажных банкнот)
     * @param r5000 для 5000 купюр
     * @param r1000 для 1000
     * @param r500 для 500
     * @param r100 для 100
     * @param r50 для 50
     * @param r10 для 10
     */
    public ATM(int r5000, int r1000, int r500, int r100, int r50, int r10) {
        atm.add(new Cash5000(r5000));
        atm.add(new Cash1000(r1000));
        atm.add(new Cash500(r500));
        atm.add(new Cash100(r100));
        atm.add(new Cash50(r50));
        atm.add(new Cash10(r10));
    }

    /**
     * возвращает суммарное количетво денег в валюте в банкомате
     * @return суммарное количетво денег в валюте в банкомате
     */
    public long summ_left() {
        long summ = 0;
        for (Money cash : atm) {
            summ += cash.showAvailableMoney();
        }
        return summ;
    }

    public List<Money> left(){
        return atm;
    }

    /**
     * снятие наличности в банкомате
     * @param amount запрашиваемое количетво денег
     * @return если запрашивается больше, чем в банокмате денег или если невозможно разбить запрашиваемую сумму по банкнотам - возвращает 0, иначе вовращает сумму
     */
    public double withdrawal(long amount) {
        if (amount > summ_left() || amount < 0) {
            throw new RuntimeException("невозможно выдать, такой суммы нет в банкомате");
        } else{
            List<Integer> withdralBills = algorythm(amount);
            double summ = 0;
            if(withdralBills!=null) {
                for (int j = 0; j < withdralBills.size(); j++) {
                    double sm = atm.get(j).getAmount(withdralBills.get(j))*atm.get(j).getType();
                    summ += sm;
                }
                return summ;
            }else {
                throw new RuntimeException("невозможно выдать, невозможно выдать ровно такую сумму");
            }
        }
    }

    /**
     * алгоритм расчета выдваемого количества банкнот в два прохода
     * первый проход - определение идеального распределения: 12570 - 2*5000+2*1000+5*100+1*50+2*10
     * второй проход - из идеального получение реального распределения,
     * например,если 5000-х нет или 1 штука, то снимается, что есть, а при анализе 1000-х купюр 2*1000
     * добавляется остаток от предыдущего анализа 5000-х купюр 1*5000=1*(5000/1000)*1000 = 5*1000
     * @param amount запрашиваемая сумма
     * @return необходимое на выдачу количество банкнот каждого номинала или null, если распределение невозможно
     */
    public List<Integer> algorythm(double amount) {
        List<Integer> distributionIdeal = new ArrayList<>();
        for(int i = 0 ; i < atm.size() ; i++){
            double remainder = amount%(atm.get(i).getType());
            distributionIdeal.add((int)((amount-remainder)/atm.get(i).getType()));
            amount = remainder;
        }
        if(amount!=0){//остался настолько маленький остаток, что его нечем оплачивать
            throw new RuntimeException("невозможно выдать, невозможно выдать ровно такую сумму");
        }

        List<Integer> distributionReal = new ArrayList<>();
        int remain = 0;
        //для банкнот кроме последней
        for(int i = 0 ; i < atm.size()-1;i++){
            if(distributionIdeal.get(i) + remain <= atm.get(i).showAvailable()){
                distributionReal.add(distributionIdeal.get(i) + remain);
                remain = 0;
            }
            else{
                distributionReal.add(atm.get(i).showAvailable());
                remain += distributionIdeal.get(i) - atm.get(i).showAvailable();
                remain = remain*((int)(atm.get(i).getType()/atm.get(i+1).getType()));
            }
        }
        //отдельно тот же алгоритм для последней банкноты, если
        int i = atm.size()-1;
        if(distributionIdeal.get(i) + remain <= atm.get(i).showAvailable()){
            distributionReal.add(distributionIdeal.get(i) + remain);
        }
        else{
            return null;
        }
        return distributionReal;
    }
}
