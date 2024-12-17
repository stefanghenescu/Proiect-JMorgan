package org.poo.commands;

import org.poo.bank.account.Account;
import org.poo.bank.account.AccountFactory;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.NoSuchElementException;

public class AddAccountCommand implements Command{
    private Bank bank;
    private CommandInput command;

    public AddAccountCommand(Bank bank, CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    @Override
    public void execute() {
        // create account
        Account account = AccountFactory.createAccount(command);

        // get email of user to add account
        String userEmail = command.getEmail();

        // get the user with the email from the command
        // add the account to that user
        User userToAddAccount;
        try {
            userToAddAccount = bank.getUser(userEmail);
        } catch (NoSuchElementException e) {
            return;
        }

        account.setOwner(userToAddAccount);
        userToAddAccount.addAccount(account);

        // add the account to the bank database
        bank.getAccounts().put(account.getIban(), account);

        // add the account to the aliases database
        bank.getAliases().put(account.getIban(), account.getIban());

        // transaction for later update()
        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                "New account created")
                .build();
        userToAddAccount.addTransaction(transaction);
        account.addTransaction(transaction);
    }
}
