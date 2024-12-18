package org.poo.commands;

import org.poo.bank.account.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

import java.util.NoSuchElementException;

public class AddFundsCommand implements Command {
    private final Bank bank;
    private final CommandInput command;

    public AddFundsCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

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
