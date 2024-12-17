package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class SetAliasCommand implements Command {
    private Bank bank;
    private CommandInput command;

    public SetAliasCommand(Bank bank, CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    @Override
    public void execute() {
        bank.getAliases().put(command.getAlias(), command.getAccount());
    }
}
