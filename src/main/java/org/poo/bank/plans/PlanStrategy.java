package org.poo.bank.plans;
import org.poo.bank.Bank;

/**
 * Interface defining strategies for calculating commission and cashback ercentages based on a
 * specific plan.
 */
public interface PlanStrategy {
    /**
     * Calculates the commission to be deducted for a transaction
     * Different plans have different commission rates and conditions
     * @param amount the amount of the transaction
     * @param bank the bank instance that provides additional information
     * @param currency the currency of the transaction. It is used to calculate the exchange rate
     * @return the commission to be deducted from the account when paying
     */
    double calculateCommission(double amount, Bank bank, String currency);

    /**
     * Calculates the cashback percentage for a transaction based on the different plans cashback
     * policies and conditions.
     * @param amount the amount of the transaction for which cashback is calculated
     * @return the cashback percentage to be added to the account
     */
    double calculateCashBackPercentage(double amount);
}
