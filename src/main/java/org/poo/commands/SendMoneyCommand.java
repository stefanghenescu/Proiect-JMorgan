package org.poo.commands;

import org.poo.bank.account.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.NoSuchElementException;

public class SendMoneyCommand implements Command {
    private static final double ROUNDING = 10000.0;
    private final Bank bank;
    private final CommandInput command;

    public SendMoneyCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    @Override
    public void execute() {
        // get the account to send money from
        String receiverAccountIBAN = bank.getAliases().get(command.getReceiver());

        Account senderAccount;
        Account receiverAccount;
        try {
            senderAccount = bank.getAccount(command.getAccount());
            receiverAccount = bank.getAccount(receiverAccountIBAN);
        } catch (NoSuchElementException e) {
            return;
        }

        // send the money
        double amountWithdrawn = senderAccount.withdraw(command.getAmount());

        // convert in receiver account currency
        double exchangeRate = bank.getExchangeRates().getRate(senderAccount.getCurrency(),
                receiverAccount.getCurrency());
        double amount = amountWithdrawn * exchangeRate;

        double amountRounded = Math.round(amount * ROUNDING) / ROUNDING;

        receiverAccount.addFunds(amount);

        Transaction transactionSender;
        Transaction transactionReceiver = null;

        if (amountWithdrawn == 0) {
            // add transaction with an error message
            transactionSender = new Transaction.TransactionBuilder(command.getTimestamp(),
                    "Insufficient funds")
                    .build();
        } else {
            transactionSender = new Transaction.TransactionBuilder(command.getTimestamp(),
                    command.getDescription())
                    .senderIBAN(senderAccount.getIban())
                    .receiverIBAN(receiverAccount.getIban())
                    .amountString(amountWithdrawn + " " + senderAccount.getCurrency())
                    .transferType("sent")
                    .build();

            transactionReceiver = new Transaction.TransactionBuilder(command.getTimestamp(),
                    command.getDescription())
                    .senderIBAN(senderAccount.getIban())
                    .receiverIBAN(receiverAccount.getIban())
                    .amountString(amountRounded + " " + receiverAccount.getCurrency())
                    .transferType("received")
                    .build();

        }
        senderAccount.getOwner().addTransaction(transactionSender);
        senderAccount.addTransaction(transactionSender);

        if (transactionReceiver != null) {
            receiverAccount.getOwner().addTransaction(transactionReceiver);
            receiverAccount.addTransaction(transactionReceiver);
        }
    }
}
