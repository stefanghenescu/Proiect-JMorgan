package org.poo.commands;

import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

public class SplitPaymentCommand implements Command {
    private Bank bank;
    private CommandInput command;

    public SplitPaymentCommand(Bank bank, CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    @Override
    public void execute() {
        double amountPerPerson = command.getAmount() / command.getAccounts().size();
        boolean everyonePaid = true;
        Transaction transaction;
        String error = null;

        for (String accountIBAN : command.getAccounts().reversed()) {
            Account account = Account.getAccount(bank, accountIBAN);

            if (account == null) {
                return;
            }

            double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(), account.getCurrency());
            everyonePaid = account.checkEnoughMoney(amountPerPerson * exchangeRate);

            if (!everyonePaid) {
                error = "Account " + accountIBAN + " has insufficient funds for a split payment.";
                break;
            }
        }

        if (everyonePaid) {
            for (String accountIBAN : command.getAccounts()) {
                Account account = Account.getAccount(bank, accountIBAN);
                double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(), account.getCurrency());
                account.withdraw(amountPerPerson * exchangeRate);
            }
        }

        // add transactions for each account
        transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                // I do like this as ref has amount with 2 decimals evan if it is an int
                // (1269.00 EUR)
                String.format("Split payment of %.2f %s", command.getAmount(), command.getCurrency()))
                .error(error)
                .currency(command.getCurrency())
                .amount(amountPerPerson)
                .involvedAccounts(command.getAccounts())
                .build();

        for (String accountIBAN : command.getAccounts()) {
            Account account = Account.getAccount(bank, accountIBAN);
            account.getOwner().addTransaction(transaction);
            account.addTransaction(transaction);
        }
    }
}
