package org.poo.bank.plans;
import org.poo.bank.Bank;

public final class StandardPlan implements PlanStrategy {
    private static final double COMMISSION_PERCENTAGE = 0.002;
    private static final double CASHBACK_PERCENTAGE_SMALL = 0.001;
    private static final double CASHBACK_PERCENTAGE_MEDIUM = 0.002;
    private static final double CASHBACK_PERCENTAGE_BIG = 0.025;
    private static final int SPEND_AMOUNT_SMALL = 100;
    private static final int SPEND_AMOUNT_MEDIUM = 300;
    private static final int SPEND_AMOUNT_BIG = 500;

    @Override
    public double calculateCommission(final double amount, final Bank bank, final String currency) {
        return amount * COMMISSION_PERCENTAGE;
    }

    @Override
    public double calculateCashBackPercentage(final double amount) {
        if (SPEND_AMOUNT_SMALL <= amount && amount < SPEND_AMOUNT_MEDIUM) {
            return CASHBACK_PERCENTAGE_SMALL;
        } else if (SPEND_AMOUNT_MEDIUM <= amount && amount < SPEND_AMOUNT_BIG) {
            return CASHBACK_PERCENTAGE_MEDIUM;
        } else if (amount >= SPEND_AMOUNT_BIG) {
            return CASHBACK_PERCENTAGE_BIG;
        }
        return 0.0;
    }
}
