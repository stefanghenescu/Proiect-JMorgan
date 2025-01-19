package org.poo.bank.accounts;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.cards.Card;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

/**
 * Class that represents a bank account. Each account a list of cards and transactions.
 * This class is then extended by the different types of accounts (classic, savings)
 */
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
    private double moneySpent = 0;


    public Account(final CommandInput commandInput) {
        iban = Utils.generateIBAN();
        balance = DEFAULT_BALANCE;
        currency = commandInput.getCurrency();
        accountType = commandInput.getAccountType();
    }

    /**
     * Method that adds a card to the account
     * @param card the card to be added
     */
    public void addCard(final Card card) {
        cards.add(card);
    }

    /**
     * Method that adds funds to the account
     * @param amount the amount of money to be added to the account
     */
    public void addFunds(final double amount) {
        balance += amount;
    }

    /**
     * Method that deletes a card from the account
     * @param card the card to be deleted
     */
    public void deleteCard(final Card card) {
        cards.remove(card);
    }

    /**
     * Method that withdraws money from the account and decreases the balance
     * @param amount the amount of money to be withdrawn
     * @return the amount of money withdrawn, or 0 if the balance is too low to withdraw and
     * still remain with the minimum balance
     */
    public double withdraw(final double amount) {
        if (!checkEnoughMoney(amount)) {
            return 0;
        }

        balance -= amount;
        return amount;
    }

    /**
     * Method that checks if the account has enough money to withdraw a certain amount and still
     * remains with the minimum balance
     * @param amount the amount of money to be withdrawn
     * @return true if the account has enough money, false otherwise
     */
    public boolean checkEnoughMoney(final double amount) {
        if (balance - amount < minBalance) {
            return false;
        }
        return true;
    }

    /**
     * Method that adds a transaction to the list of transactions for the account
     * @param transaction the transaction to be added
     */
    public void addTransaction(final Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    public void addMoneySpent(double moneySpent) {
        this.moneySpent += moneySpent;
    }
    /**
     * This method will add interest rate for a savings account and for a classic account will
     * print an error.
     * @param command input that contains information about the interest rate
     * @param output where the message will be printed
     */
    public abstract void addInterestRate(CommandInput command, ArrayNode output);


    /**
     * This method will change the interest rate for a savings account and for a classic account
     * will print an error.
     * @param command input that contains information about the interest rate
     * @param output where the message will be printed
     */
    public abstract void changeInterestRate(CommandInput command, ArrayNode output);
}

