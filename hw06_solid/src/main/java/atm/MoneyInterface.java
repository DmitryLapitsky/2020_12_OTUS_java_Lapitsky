package atm;

public interface MoneyInterface {

    double getType();

    int getAmount(int numberOfBills);

    int showAvailable();

    double showAvailableMoney();

    String toString();
}
