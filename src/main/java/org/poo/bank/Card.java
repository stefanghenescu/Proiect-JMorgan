package org.poo.bank;

import lombok.Getter;
import lombok.Setter;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

@Setter
@Getter
public class Card {
    private String status;
    private String number;
    private Account owner;

    public Card(Account ownerAccount) {
        number = Utils.generateCardNumber();
        status = "active";
        owner = ownerAccount;
    }

    public static Card getCard(SetupBank bank, String number) {
        return bank.getCards().get(number);
    }

    public void payOnline(double amount, long timestamp, String commerciant) {
        Transaction transaction;
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
        }

        owner.getOwner().addTransaction(transaction);
    }
}
