package org.poo.bank.cards;

import org.poo.bank.account.Account;
import org.poo.transactions.Transaction;

public class CardOneTimeUse extends Card {
    private static final int DIF_BALANCE = 30;
    public CardOneTimeUse(Account ownerAccount) {
        super(ownerAccount);
    }

    @Override
    public boolean payOnline(double amount, long timestamp, String commerciant) {
        Transaction transaction;
        boolean statusPayment = false;
        if (getStatus().equals("frozen")) {
            // add transaction with an error message
            transaction = new Transaction.TransactionBuilder(timestamp,
                    "The card is frozen")
                    .build();
        }else if (getOwner().withdraw(amount) == 0) {
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
            statusPayment = true;
        }

        // add transaction to the owner of the account that owns the card :)
        getOwner().getOwner().addTransaction(transaction);

        // add transaction to the account
        getOwner().addTransaction(transaction);

        return statusPayment;
    }

    public void check(long timestamp) {
        if (getOwner().getBalance() <= getOwner().getMinBalance()) {
            freeze(timestamp);
        } else if (getOwner().getBalance() - getOwner().getMinBalance() <= DIF_BALANCE) {
            warning();
        }
    }

    private void freeze(long timestamp) {
        setStatus("frozen");
        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                "You have reached the minimum amount of funds, the card will be frozen")
                .build();

        // add transaction to the owner of the account that owns the card :)
        getOwner().getOwner().addTransaction(transaction);

        // add transaction to the account
        getOwner().addTransaction(transaction);
    }

    private void warning() {
        setStatus("warning");
    }
}
