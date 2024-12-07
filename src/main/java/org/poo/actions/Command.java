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
        User userToAddAccount = User.getUser(bank, userEmail);
        if (userToAddAccount == null)
            return;

        userToAddAccount.addAccount(account);

        // add the account to the bank database
        bank.getAccounts().put(account.getIban(), account);

        // transaction for later update()
    }

    public static void addFunds(SetupBank bank, CommandInput command) {
        // get the account to add funds to
        Account account = Account.getAccount(bank, command.getAccount());

        if (account == null) {
            return;
        }

        // add the funds to the account
        account.addFunds(command.getAmount());
    }

    public static void createCard(SetupBank bank, CommandInput command) {
        // get the account to add the card to
        Account account = Account.getAccount(bank, command.getAccount());

        User user = User.getUser(bank, command.getEmail());
        if (user == null)
            return;

        if (!user.getAccounts().contains(account)) {
            // update() transactions with an error message
            return;
            //throw new IllegalArgumentException("User does not own the account");
        }

        Card card;
        if (command.getCommand().equals("createOneTimeCard")) {
            card = new CardOneTimeUse(account);
        } else {
            card = new Card(account);
        }

        // add the card to the account
        account.addCard(card);

        // add the card to the bank database
        bank.getCards().put(card.getNumber(), card);

        // transaction for later update()
    }

    public static void deleteAccount(SetupBank bank, CommandInput command, ArrayNode output) {
        // get the account to delete
        Account account = Account.getAccount(bank, command.getAccount());

        if (account == null) {
            return;
        }

        // get the user to delete the account from
        User user = User.getUser(bank, command.getEmail());

        if (user == null) {
            return;
        }

        // delete the account from the user
        user.deleteAccount(account);

        // delete the account from the bank database
        bank.getAccounts().remove(account.getIban());

        output.add(JsonNode.eraseAccount(command, account));

        // transaction for later update()
    }

    public static void deleteCard(SetupBank bank, CommandInput command) {
        // get the card to delete
        Card card = Card.getCard(bank, command.getCardNumber());

        if (card == null) {
            return;
        }

        // delete the card from the account
        Account ownerAccount = card.getOwner();
        ownerAccount.deleteCard(card);

        // delete the card from the bank database
        bank.getCards().remove(card.getNumber());

        // transaction for later update()
    }

    public static void setMinBalance(SetupBank bank, CommandInput command) {
        // get the account to set the minimum balance
        Account account = Account.getAccount(bank, command.getAccount());

        if (account == null) {
            return;
        }

        // set the minimum balance for the account
        account.setMinBalance(command.getAmount());
    }

    public static void payOnline(SetupBank bank, CommandInput command, ArrayNode output) {
        Card card = Card.getCard(bank, command.getCardNumber());
        User cardOwnerUser = User.getUser(bank, command.getEmail());

        if (card == null) {
            output.add(JsonNode.cardNotFound(command));
            return;
        }

        Account account = card.getOwner();
        if (!cardOwnerUser.getAccounts().contains(account)) {
            throw new IllegalArgumentException("User does not own the account");
        }

        // convert in account currency
        double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(), account.getCurrency());
        double amount = command.getAmount() * exchangeRate;

        card.payOnline(amount);
        // commerciant money sent
        // transaction for later update()
    }

    public static void sendMoney(SetupBank bank, CommandInput command) {
        // get the account to send money from
        Account senderAccount = Account.getAccount(bank, command.getAccount());
        Account receiverAccount = Account.getAccount(bank, command.getReceiver());

        if (senderAccount == null || receiverAccount == null) {
            return;
        }

        // send the money
        double amountWithdrawn = senderAccount.withdraw(command.getAmount());

        // convert in receiver account currency
        double exchangeRate = bank.getExchangeRates().getRate(senderAccount.getCurrency(),
                receiverAccount.getCurrency());
        double amount = amountWithdrawn * exchangeRate;

        receiverAccount.addFunds(amount);

        // transaction for later update()
    }
}
