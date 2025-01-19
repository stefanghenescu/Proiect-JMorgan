package org.poo.bank.commerciants;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;

public interface CashbackStrategy {
    void cashback(final double amount, final Account account, final Bank bank);
}
