package org.poo.commands;

import org.poo.bank.accounts.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.NoSuchElementException;

/**
 * Class responsible for sending money from one account to another.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class SendMoneyCommand implements Command {
    private static final double ROUNDING = 10000.0;
    private final Bank bank;
    private final CommandInput command;

    public SendMoneyCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    /**
     * Method responsible for sending money from one account to another.
     * If the sender account does not have enough money, an error message is added to the output.
     * Take into account the exchange rate between the sender and receiver accounts.
     * Add transactions to the sender and receiver accounts.
     */
    @Override
    public void execute() {
        /**
         * get the account to send money from
         * aliases have the IBAN of the account and also the aliases of the account as keys for
         * the same IBAN
         */
        String receiverAccountIBAN = bank.getAliases().get(command.getReceiver());

        Account senderAccount;
        Account receiverAccount;
        try {
            senderAccount = bank.getAccount(command.getAccount());
            receiverAccount = bank.getAccount(receiverAccountIBAN);
        } catch (NoSuchElementException e) {
            return;
        }

        // take the money from the sender account
        double amountWithdrawn = senderAccount.withdraw(command.getAmount());

        // convert in receiver account currency
        double exchangeRate = bank.getExchangeRates().getRate(senderAccount.getCurrency(),
                receiverAccount.getCurrency());
        double amount = amountWithdrawn * exchangeRate;

        double amountRounded = Math.round(amount * ROUNDING) / ROUNDING;

        // add the money to the receiver account
        receiverAccount.addFunds(amount);

        Transaction transactionSender;
        Transaction transactionReceiver = null;

        // if the sender does not have enough money, add a transaction with an error message
        if (amountWithdrawn == 0) {
            // add transaction with an error message
            transactionSender = new Transaction.TransactionBuilder(command.getTimestamp(),
                    "Insufficient funds")
                    .build();
        } else {
            // add transaction for sender and receiver
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

        // if money can be sent (the sender has enough money), add a transaction to the receiver
        if (transactionReceiver != null) {
            receiverAccount.getOwner().addTransaction(transactionReceiver);
            receiverAccount.addTransaction(transactionReceiver);
        }
    }
}
