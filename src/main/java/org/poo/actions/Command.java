package org.poo.actions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonNode;

import java.util.ArrayList;

public class Command {
    public static void printUsers (SetupBank bank, CommandInput command, ArrayNode output) {
        ObjectNode usersArray = JsonNode.writeUsers(command, bank);
        output.add(usersArray);
    }

    public static void addAccount(SetupBank bank, CommandInput command) {
        // create account
        Account account = AccountFactory.createAccount(command);

        // get email of user to add account
        String userEmail = command.getEmail();

        // get the user with the email from the command
        // add the account to that user
        User userToAddAccount = bank.getUsers().get(userEmail);
        if (userToAddAccount == null)
            return;

        userToAddAccount.addAccount(account);

        // add the account to the bank database
        bank.getAccounts().put(account.getIban(), account);

        // transaction for later update()
    }

    public static void addFunds(SetupBank bank, CommandInput command) {
        // get the account to add funds to
        String iban = command.getAccount();
        Account account = bank.getAccounts().get(iban);

        if (account == null) {
            return;
        }

        // add the funds to the account
        account.addFunds(command.getAmount());
    }

    public static void createCard(SetupBank bank, CommandInput command) {
        // get the account to add the card to
        String iban = command.getAccount();
        Account account = bank.getAccounts().get(iban);

        String userEmail = command.getEmail();

        User user = bank.getUsers().get(userEmail);
        if (user == null)
            return;

        if (!user.getAccounts().contains(account)) {
            // update() transactions with an error message
            return;
            //throw new IllegalArgumentException("User does not own the account");
        }

        // create the card
        Card card = new Card(account);

        // add the card to the account
        account.addCard(card);

        // add the card to the bank database
        bank.getCards().put(card.getNumber(), card);

        // transaction for later update()
    }

    public static void deleteAccount(SetupBank bank, CommandInput command, ArrayNode output) {
        // get the account to delete
        String iban = command.getAccount();
        Account account = bank.getAccounts().get(iban);

        if (account == null) {
            return;
        }

        // get the user to delete the account from
        String userEmail = command.getEmail();
        User user = bank.getUsers().get(userEmail);

        if (user == null) {
            return;
        }

        // delete the account from the user
        user.deleteAccount(account);

        // delete the account from the bank database
        bank.getAccounts().remove(iban);

        output.add(JsonNode.eraseAccount(command, account));

        // transaction for later update()
    }

    public static void deleteCard(SetupBank bank, CommandInput command) {
        // get the account to delete the card from
        String cardNumber = command.getCardNumber();
        Card card = bank.getCards().get(cardNumber);

        if (card == null) {
            return;
        }

        // delete the card from the account
        Account ownerAccount = card.getOwner();
        ownerAccount.deleteCard(card);

        // delete the card from the bank database
        bank.getCards().remove(cardNumber);

        // transaction for later update()
    }
}
