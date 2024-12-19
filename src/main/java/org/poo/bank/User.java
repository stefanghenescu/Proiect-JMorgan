package org.poo.bank;

import lombok.Getter;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.fileio.UserInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

@Getter
/**
 * Class that represents a user. Each user has a list of accounts and transactions.
 */
public class User {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final ArrayList<Account> accounts = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public User(final UserInput userInput) {
        firstName = userInput.getFirstName();
        lastName = userInput.getLastName();
        email = userInput.getEmail();
    }

    /**
     * Method that adds an account to the user. This method is used when creating a new account.
     * @param account the account to be added to the user's database
     */
    public void addAccount(Account account) {
        accounts.add(account);
    }

    /**
     * Deletes an account from the user's list of accounts if its balance is zero.
     * All associated cards will also be deleted before the account is removed.
     * @param account the account to be removed from the user's database
     * @return true if the account was successfully deleted, false if the account's balance is non-zero
     */
    public boolean deleteAccount(final Account account) {
        if (account.getBalance() != 0) {
            return false;
        }

        for (Card card : account.getCards()) {
            account.deleteCard(card);
        }
        accounts.remove(account);
        return true;
    }

    /**
     * Method that adds a transaction to the user's transaction history.
     * @param transaction the transaction to be added
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }
}
