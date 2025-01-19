package org.poo.bank.plans;
import org.poo.bank.Bank;

public class SilverPlan implements PlanStrategy {
    @Override
    public double calculateCommission(final double amount, final Bank bank, final String currency) {
        double ronExchange = bank.getExchangeRates().getRate(currency, "RON");
        if (amount * ronExchange >= 500) {
            return amount * 0.001;
        }
        return 0.0;
    }

    public double calculateCashBackPercentage(final double amount) {
        if (100 <= amount && amount < 300) {
            return 0.003;
        } else if (300 <= amount && amount < 500) {
            return 0.004;
        } else if (amount >= 500) {
            return 0.05;
        }
        return 0.0;
    }
}
