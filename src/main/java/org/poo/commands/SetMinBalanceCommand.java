package org.poo.commands;

import org.poo.bank.accounts.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

import java.util.NoSuchElementException;

public class SetMinBalanceCommand implements Command {
    private final Bank bank;
    private final CommandInput command;

    public SetMinBalanceCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    @Override
    public void execute() {
        String accountIBAN = bank.getAliases().get(command.getAccount());

        // get the account to set the minimum balance
        Account account;
        try {
            account = bank.getAccount(accountIBAN);
        } catch (NoSuchElementException e) {
            return;
        }

        // set the minimum balance for the account
        account.setMinBalance(command.getAmount());
    }
}
