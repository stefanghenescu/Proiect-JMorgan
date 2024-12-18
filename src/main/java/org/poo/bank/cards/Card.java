package org.poo.bank.cards;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.account.Account;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

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
            transaction = new Transaction.TransactionBuilder(timestamp,
                    "Card payment")
                    .amount(amount)
                    .commerciant(commerciant)
                    .build();

            successPayment = true;
        }

        owner.getOwner().addTransaction(transaction);
        owner.addTransaction(transaction);
        return successPayment;
    }

    public void check(final long timestamp) {
        if (owner.getBalance() <= owner.getMinBalance()) {
            freeze(timestamp);
        } else if (owner.getBalance() - owner.getMinBalance() <= DIF_BALANCE) {
            warning();
        }
    }

    protected void freeze(final long timestamp) {
        status = "frozen";
        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                "You have reached the minimum amount of funds, the card will be frozen")
                .build();

        owner.getOwner().addTransaction(transaction);
        owner.addTransaction(transaction);
    }

    protected void warning() {
        status = "warning";
    }
}
