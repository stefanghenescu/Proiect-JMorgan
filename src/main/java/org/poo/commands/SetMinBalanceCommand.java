package org.poo.commands;

import org.poo.bank.account.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class SetMinBalanceCommand implements Command {
    private Bank bank;
    private CommandInput command;

    public SetMinBalanceCommand(Bank bank, CommandInput command) {
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
        } catch (NullPointerException e) {
            return;
        }

        // set the minimum balance for the account
        account.setMinBalance(command.getAmount());
    }
}
