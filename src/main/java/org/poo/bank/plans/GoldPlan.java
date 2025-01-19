package org.poo.bank.plans;

import org.poo.bank.Bank;

public class GoldPlan implements PlanStrategy {
    @Override
    public double calculateCommission(final double amount, final Bank bank, String currency) {
        return 0.0;
    }

    public double calculateCashBackPercentage(final double amount) {
        if (100 <= amount && amount < 300) {
            return 0.005;
        } else if (300 <= amount && amount < 500) {
            return 0.055;
        } else if (amount >= 500){
            return 0.07;
        }
        return 0.0;
    }
}

