package org.poo.bank.commerciants;

import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;

public final class SpendingThresholdCashback implements CashbackStrategy {
    @Override
    public void cashback(final double amount, final Account account, final Bank bank) {
        User user = account.getOwner();

        double exchangeToRon = bank.getExchangeRates().getRate(account.getCurrency(), "RON");

        account.addMoneySpent(amount * exchangeToRon);

        double amountSpent = account.getMoneySpent();
        double cashbackPercentage = user.getPlanStrategy().calculateCashBackPercentage(amountSpent);

        account.addFunds(amount * cashbackPercentage);
    }
}
