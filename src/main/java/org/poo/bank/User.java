package org.poo.bank;

import lombok.Getter;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Getter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts = new ArrayList<>();

    public User(UserInput userInput) {
        firstName = userInput.getFirstName();
        lastName = userInput.getLastName();
        email = userInput.getEmail();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void deleteAccount(Account account) {
        if (account.getBalance() != 0) {
            return;
        }

        for (Card card : account.getCards()) {
            account.addCard(card);
        }
        accounts.remove(account);
    }
}
