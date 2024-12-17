package org.poo.bank.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.cards.Card;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Setter
@Getter
public class Account {
    public static final double DEFAULT_BALANCE = 0.0;
    private String iban;
    private double balance;
    private double minBalance;
    private String currency;
    private String accountType;
    private ArrayList<Card> cards = new ArrayList<>();
    private User owner;
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Account(final CommandInput commandInput) {
        iban = Utils.generateIBAN();
        balance = DEFAULT_BALANCE;
        currency = commandInput.getCurrency();
        accountType = commandInput.getAccountType();
    }

    public void addCard(final Card card) {
        cards.add(card);
    }

    public void addFunds(final double amount) {
        balance += amount;
    }

    public void deleteCard(final Card card) {
        cards.remove(card);
    }

    public double withdraw(final double amount) {
        if (balance - amount <= minBalance) {
            return 0;
        }
        balance -= amount;
        return amount;
    }

    public boolean checkEnoughMoney(final double amount) {
        if (balance - amount <= minBalance) {
            return false;
        }
        return true;
    }

    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }
}

