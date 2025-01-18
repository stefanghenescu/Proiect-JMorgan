package org.poo.bank.commission;
import org.poo.bank.Bank;

public class StandardCommission implements CommissionStrategy {
    @Override
    public double calculateCommission(final double amount, final Bank bank, final String currency) {
        return amount * 0.002;
    }
}
