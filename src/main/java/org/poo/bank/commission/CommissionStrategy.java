package org.poo.bank.commission;

import org.poo.bank.Bank;

public interface CommissionStrategy {
    double calculateCommission(double amount, Bank bank, String currency);
}
