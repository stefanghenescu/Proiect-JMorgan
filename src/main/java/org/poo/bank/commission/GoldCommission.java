package org.poo.bank.commission;

import org.poo.bank.Bank;

public class GoldCommission implements CommissionStrategy {
    @Override
    public double calculateCommission(final double amount, final Bank bank, String currency) {
        return 0.0;
    }
}
