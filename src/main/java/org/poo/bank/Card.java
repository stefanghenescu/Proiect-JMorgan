package org.poo.bank;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

@Setter
@Getter
public class Card {
    private static final int DIF_BALANCE = 30;
    private String status;
    private String number;
    private Account owner;

    public Card(Account ownerAccount) {
        number = Utils.generateCardNumber();
        status = "active";
        owner = ownerAccount;
    }

    public static Card getCard(Bank bank, String number) {
        return bank.getCards().get(number);
    }

    public boolean payOnline(double amount, long timestamp, String commerciant) {
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
        owner.addTransaction(transaction);

        return false;
    }

    public void check(long timestamp) {
        if (owner.getBalance() <= owner.getMinBalance()) {
            freeze(timestamp);
        } else if (owner.getBalance() - owner.getMinBalance() <= DIF_BALANCE) {
            warning();
        }
    }

    private void freeze(long timestamp) {


        status = "frozen";
        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                "You have reached the minimum amount of funds, the card will be frozen")
                .build();

        owner.getOwner().addTransaction(transaction);
        owner.addTransaction(transaction);
    }

    private void warning() {
        status = "warning";
    }
}
