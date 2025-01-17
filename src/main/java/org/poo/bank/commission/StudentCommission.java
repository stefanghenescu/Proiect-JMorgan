package org.poo.bank.commission;

import org.poo.bank.Bank;

public class StudentCommission implements CommissionStrategy {
    @Override
    public double calculateCommission(final double amount, final Bank bank, final String currency) {
        return 0.0;
    }
}
