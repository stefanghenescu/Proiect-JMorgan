package org.poo.bank.plans;
import org.poo.bank.Bank;

public class StandardPlan implements PlanStrategy {
    @Override
    public double calculateCommission(final double amount, final Bank bank, final String currency) {
        return amount * 0.002;
    }

    public double calculateCashBackPercentage(final double amount) {
        if (100 <= amount && amount < 300) {
            return 0.001;
        } else if (300 <= amount && amount < 500) {
            return 0.002;
        } else if (amount >= 500){
            return 0.025;
        }
        return 0.0;
    }
}
