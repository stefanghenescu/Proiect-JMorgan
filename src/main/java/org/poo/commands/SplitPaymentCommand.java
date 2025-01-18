package org.poo.commands;

import org.poo.bank.accounts.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.NoSuchElementException;

/**
 * Class responsible for splitting a payment between multiple accounts.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class SplitPaymentCommand implements Command {
    private static final String SPLIT_PAYMENT_MESSAGE = "Split payment of %.2f %s";
    private final Bank bank;
    private final CommandInput command;

    public SplitPaymentCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    /**
     * Method responsible for splitting a payment between multiple accounts.
     * If any account does not have enough funds, an error transaction will be generated for every
     * account. If all accounts have enough funds, the payment will be split between them.
     */
    @Override
    public void execute() {
        double amountPerPerson = command.getAmount() / command.getAccounts().size();
        boolean everyonePaid = true;
        Transaction transaction;
        String error = null;

        // iterate over accounts to withdraw money from them
        for (String accountIBAN : command.getAccounts().reversed()) {
            Account account;
            try {
                account = bank.getAccount(accountIBAN);
            } catch (NoSuchElementException e) {
                return;
            }

            double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(),
                                                                    account.getCurrency());

            // check if account has enough money
            // if not, set error and break
            everyonePaid = account.checkEnoughMoney(amountPerPerson * exchangeRate);

            if (!everyonePaid) {
                error = "Account " + accountIBAN + " has insufficient funds for a split payment.";
                break;
            }
        }

        // if everyone has enough money, withdraw the amount from each account
        if (everyonePaid) {
            for (String accountIBAN : command.getAccounts()) {
                Account account = bank.getAccount(accountIBAN);
                double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(),
                                                                        account.getCurrency());
                account.withdraw(amountPerPerson * exchangeRate);
            }
        }

        // if there was an error, add an error for each account in the transaction
        // otherwise, add the transaction for splitting payment to each account
        transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                // I do like this as ref has amount with 2 decimals even if it is an int
                // (1269.00 EUR)
                String.format(SPLIT_PAYMENT_MESSAGE, command.getAmount(), command.getCurrency()))
                .error(error)
                .currency(command.getCurrency())
                .amount(amountPerPerson)
                .involvedAccounts(command.getAccounts())
                .build();

        // add transaction to each account
        for (String accountIBAN : command.getAccounts()) {
            Account account = bank.getAccount(accountIBAN);
            account.getOwner().addTransaction(transaction);
            account.addTransaction(transaction);
        }
    }
}
