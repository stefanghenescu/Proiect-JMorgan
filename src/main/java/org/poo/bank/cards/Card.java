package org.poo.bank.cards;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

/**
 * Class that represents a card.
 */
@Setter
@Getter
public class Card {
    private static final int DIF_BALANCE = 30;
    private String status;
    private String number;
    private Account owner;

    public Card(final Account ownerAccount) {
        number = Utils.generateCardNumber();
        status = "active";
        owner = ownerAccount;
    }

    /**
     * Method for paying online with the card. This method in different for each type of card.
     * Moreover, it adds a transaction to the owner of the card (account) and to the owner of the
     * account (user).
     * @param amount the amount of money to be paid
     * @param command the command that contains the information about the payment
     * @return true if the payment was successful, false otherwise
     */
    public boolean payOnline(final double amount, final CommandInput command) {
        Transaction transaction;
        long timestamp = command.getTimestamp();
        String commerciant = command.getCommerciant();
        boolean successPayment = false;

        if (status.equals("frozen")) {
            // add transaction with an error message
            transaction = new Transaction.TransactionBuilder(timestamp,
                    "The card is frozen")
                    .build();
        } else if (owner.withdraw(amount) == 0) {
            // add transaction with an error message
            transaction = new Transaction.TransactionBuilder(timestamp,
                    "Insufficient funds")
                    .build();
        } else {
            // add transaction with the payment
            transaction = new Transaction.TransactionBuilder(timestamp,
                    "Card payment")
                    .amount(amount)
                    .commerciant(commerciant)
                    .build();

            successPayment = true;
        }

        // owner is the account and owner.getOwner() is the user to where the transaction is added
        owner.getOwner().addTransaction(transaction);
        owner.addTransaction(transaction);
        return successPayment;
    }

    /**
     * Method that checks if the card needs to be frozen or if the status has to become warning.
     * @param timestamp the timestamp of the check. It is used for when adding a transaction in
     *                  order to know when the card was frozen
     */
    public void check(final long timestamp) {
        if (owner.getBalance() <= owner.getMinBalance()) {
            freeze(timestamp);
        } else if (owner.getBalance() - owner.getMinBalance() <= DIF_BALANCE) {
            warning();
        }
    }

    /**
     * Method that freezes the card and adds a transaction to the owner of the card (account)
     * that the card was frozen.
     * @param timestamp the timestamp of the freeze
     */
    protected void freeze(final long timestamp) {
        status = "frozen";
        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                "You have reached the minimum amount of funds, the card will be frozen")
                .build();

        owner.getOwner().addTransaction(transaction);
        owner.addTransaction(transaction);
    }

    /**
     * Method that sets the status of the card to warning as the balance is close to the minimum
     */
    protected void warning() {
        status = "warning";
    }
}
