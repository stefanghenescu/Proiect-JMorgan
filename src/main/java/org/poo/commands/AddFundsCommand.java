package org.poo.commands;

import org.poo.bank.accounts.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

import java.util.NoSuchElementException;

/**
 * Class that represents the command to add funds to an account.
 * This class implements the Command interface as part of the Command design pattern.
 */
public final class AddFundsCommand implements Command {
    private final Bank bank;
    private final CommandInput command;

    public AddFundsCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    /**
     * This method executes the process of adding funds to an account.
     * If the account cannot be found, the command will terminate without changes.
     */
    @Override
    public void execute() {
        // get the account to add funds to
        Account account;
        try {
            account = bank.getAccount(command.getAccount());
        } catch (NoSuchElementException e) {
            return;
        }

        // add the funds to the account
        account.addFunds(command.getAmount());
    }
}
