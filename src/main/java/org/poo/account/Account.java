package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Card;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Setter
@Getter
public abstract class Account {
    public static final double DEFAULT_BALANCE = 0.0;
    private String iban;
    private double balance;
    private double minBalance;
    private String currency;
    private String accountType;
    private ArrayList<Card> cards = new ArrayList<>();
    private User owner;
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Account(CommandInput commandInput) {
        iban = Utils.generateIBAN();
        balance = DEFAULT_BALANCE;
        currency = commandInput.getCurrency();
        accountType = commandInput.getAccountType();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void addFunds(double amount) {
        balance += amount;
    }

    public void deleteCard(Card card) {
        cards.remove(card);
    }

    public static Account getAccount(Bank bank, String iban) {
        return bank.getAccounts().get(iban);
    }

    public double withdraw(double amount) {
        if (balance - amount <= minBalance) {
            return 0;
        }
        balance -= amount;
        return amount;
    }

    public boolean checkEnoughMoney(double amount) {
        if (balance - amount <= minBalance) {
            return false;
        }
        return true;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}

