package homework;


import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

//    Map<Customer, String> map = new TreeMap<>(new Comparator<Customer>() {
//        @Override
//        public int compare(Customer customer, Customer t1) {
//            if (customer.getScores() < t1.getScores())
//                return -1;
//            else if (customer.getScores() > t1.getScores()) {
//                return 1;
//            }
//            return 0;
//        }
//    });

    private final NavigableMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk

//        if (map.size() == 0) {
//            return null;
//        }
//        for (Map.Entry<Customer, String> el : map.entrySet()) {
//        Customer smallesCustomer = new Customer(el.getKey().getId(), el.getKey().getName(), el.getKey().getScores());
//    }
        Map.Entry<Customer, String> firstEntry = map.firstEntry();
            Customer smallesCustomer = new Customer(firstEntry.getKey().getId(), firstEntry.getKey().getName(), firstEntry.getKey().getScores());
            String info = firstEntry.getValue();
            return new Map.Entry<>() {
                @Override
                public Customer getKey() {
                    return smallesCustomer;
                }

                @Override
                public String getValue() {
                    return firstEntry.getValue();
                }

                @Override
                public String setValue(String s) {
                    return info;
                }

                @Override
                public boolean equals(Object o) {
                    if(o instanceof Customer)
                        return smallesCustomer.equals(o);
                    return false;
                }

                @Override
                public int hashCode() {
                    return smallesCustomer.hashCode();
                }
            };
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        for (Map.Entry<Customer, String> el : map.entrySet()) {
            if (el.getKey().getScores() > customer.getScores()) {
                return el;
            }
        }
        return null;
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}