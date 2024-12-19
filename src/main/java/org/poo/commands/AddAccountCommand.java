package org.poo.commands;

import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.AccountFactory;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.NoSuchElementException;

/**
 * Class for adding a new account to a user and the bank's database.
 * This command creates an account based on input. This method is part of the Command design
 * pattern.
 */
public final class AddAccountCommand implements Command {
    private final Bank bank;
    private final CommandInput command;

    public AddAccountCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    /**
     * This method executes the process of adding a new account.
     * If the specified user cannot be found, the command will terminate without changes.
     */
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

        // transaction for new account creation
        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                "New account created")
                .build();

        // add the transaction to the user and account
        userToAddAccount.addTransaction(transaction);
        account.addTransaction(transaction);
    }
}
