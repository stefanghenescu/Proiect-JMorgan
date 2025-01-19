package org.poo.bank.plans;

import org.poo.bank.Bank;

public interface PlanStrategy {
    double calculateCommission(double amount, Bank bank, String currency);
    double calculateCashBackPercentage(double amount);
}
