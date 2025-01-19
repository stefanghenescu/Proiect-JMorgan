package org.poo.bank.commerciants;

import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;

public class SpendingThresholdCashback implements CashbackStrategy {
    @Override
    public void cashback(final double amount, final Account account, final Bank bank) {
        User user = account.getOwner();

        double exchangeToRon = bank.getExchangeRates().getRate(account.getCurrency(), "RON");

        account.addMoneySpent(amount * exchangeToRon);

        double amountInRon = account.getMoneySpent();
        double cashbackPercentage = user.getPlanStrategy().calculateCashBackPercentage(amountInRon);

        account.addFunds(amount * cashbackPercentage);
    }
}
