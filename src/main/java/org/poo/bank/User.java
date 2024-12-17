package org.poo.bank;

import lombok.Getter;
import org.poo.bank.account.Account;
import org.poo.bank.cards.Card;
import org.poo.fileio.UserInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    public User(final UserInput userInput) {
        firstName = userInput.getFirstName();
        lastName = userInput.getLastName();
        email = userInput.getEmail();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

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

    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }
}
