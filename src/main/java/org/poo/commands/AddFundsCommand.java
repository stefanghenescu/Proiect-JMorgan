package org.poo.commands;

import org.poo.bank.account.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class AddFundsCommand implements Command {
    private Bank bank;
    private CommandInput command;

    public AddFundsCommand(Bank bank, CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    @Override
    public void execute() {
        // get the account to add funds to
        Account account = bank.getAccount(command.getAccount());

        if (account == null) {
            return;
        }

        // add the funds to the account
        account.addFunds(command.getAmount());
    }
}
