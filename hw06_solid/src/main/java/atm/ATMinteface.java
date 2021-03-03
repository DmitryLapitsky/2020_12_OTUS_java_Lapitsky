package atm;

import java.util.List;

public interface ATMinteface {

    long summ_left();

    List<Money> left();

    double withdrawal(long amount);
}
