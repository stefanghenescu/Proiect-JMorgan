package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.bank.Bank;
import org.poo.bank.commerciants.Commerciant;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

/**
 * Class responsible for sending money from one account to another.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class SendMoneyCommand implements Command {
    private static final double ROUNDING = 10000.0;
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public SendMoneyCommand(final Bank bank, final CommandInput command, final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for sending money from one account to another.
     * If the sender account does not have enough money, an error message is added to the output.
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
        Account receiverAccount = null;

        try {
            bank.getUser(command.getEmail());
        } catch (NoSuchElementException e) {
            output.add(JsonOutput.userNotFound(command));
            return;
        }

        try {
            senderAccount = bank.getAccount(command.getAccount());
        } catch (NoSuchElementException e) {
            return;
        }

        try {
            receiverAccount = bank.getAccount(receiverAccountIBAN); // Throws exception if not found
        } catch (NoSuchElementException e) {
            // the receiver is not a user, check if it is a commerciant
            Commerciant commerciant;
            try {
                commerciant = bank.getCommerciantByAccount(receiverAccountIBAN);
            } catch (NoSuchElementException noCommerciant) {
                output.add(JsonOutput.userNotFound(command));
                return; // Receiver not found and not a commerciant
            }
            User user = senderAccount.getOwner();

            double commission = user.getPlanStrategy().calculateCommission(command.getAmount(),
                                                                bank, senderAccount.getCurrency());

            // take the money from the sender account with the commission
            double amountWithdrawn = senderAccount.withdraw(command.getAmount() + commission);

            if (amountWithdrawn == 0 && command.getAmount() != 0) {
                // Add a transaction with an error message
                Transaction transactionSender = new Transaction.TransactionBuilder(
                        command.getTimestamp(), "Insufficient funds")
                        .build();
                senderAccount.getOwner().addTransaction(transactionSender);
                senderAccount.addTransaction(transactionSender);
                return;
            }

            // Calculate cashback
            commerciant.getCashbackStrategy().cashback(command.getAmount(), senderAccount, bank);
            return;
        }

        // the receiver is a user
        User user = senderAccount.getOwner();

        double commission = user.getPlanStrategy().calculateCommission(command.getAmount(), bank,
                senderAccount.getCurrency());

        // take the money from the sender account
        double amountWithdrawn = senderAccount.withdraw(command.getAmount() + commission);

        Transaction transactionSender;
        Transaction transactionReceiver = null;

        // if the sender does not have enough money, add a transaction with an error message
        if (amountWithdrawn <= 0 && command.getAmount() != 0) {
            // add transaction with an error message
            transactionSender = new Transaction.TransactionBuilder(command.getTimestamp(),
                    "Insufficient funds")
                    .build();
        } else {
            amountWithdrawn -= commission;

            // convert in receiver account currency
            double exchangeRate = bank.getExchangeRates().getRate(senderAccount.getCurrency(),
                    receiverAccount.getCurrency());

            double amount = amountWithdrawn * exchangeRate;

            double amountRounded = Math.round(amount * ROUNDING) / ROUNDING;

            // add the money to the receiver account
            receiverAccount.addFunds(amount);
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
