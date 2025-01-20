package org.poo.bank.commerciants;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;

/**
 * Cashback strategy interface
 */
public interface CashbackStrategy {
    /**
     * Cashback method that is implemented by 2 different cashback strategies
     * Different strategies calculate cashback percentages differently and have different conditions
     * @param amount the amount of the transaction for which cashback is calculated
     * @param account the account the cashback will be added to
     * @param bank the bank instance that provides additional information about the account and the
     *             exchange rates
     */
    void cashback(double amount, Account account, Bank bank);
}
