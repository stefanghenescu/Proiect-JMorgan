package org.poo.actions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.AccountFactory;
import org.poo.bank.SetupBank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class Command {
    public static void printUsers (SetupBank bank, CommandInput command, ArrayNode output) {

    }

    public static void addAccount(SetupBank bank, CommandInput command) {
        // create account
        Account account = AccountFactory.createAccount(command);

        // get email of user to add account
        String userEmail = command.getEmail();

        // get the user with the email from the command
        // add the account to that user
        User userToAddAccount = bank.getUsers().get(userEmail);
        userToAddAccount.addAccount(account);

        // add the account to the bank database
        bank.getAccounts().put(account.getIban(), account);

        // transaction for later update()
    }
}
