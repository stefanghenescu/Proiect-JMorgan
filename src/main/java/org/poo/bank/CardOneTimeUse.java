package org.poo.bank;

import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

public class CardOneTimeUse extends Card {
    public CardOneTimeUse(Account ownerAccount) {
        super(ownerAccount);
    }

    @Override
    public void payOnline(double amount, long timestamp, String commerciant) {
        Transaction transaction;
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

            // new card is generated
            setNumber(Utils.generateCardNumber());
        }

        // add transaction to the owner of the account that owns the card :))
        getOwner().getOwner().addTransaction(transaction);

    }
}
