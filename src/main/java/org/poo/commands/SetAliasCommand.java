package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class SetAliasCommand implements Command {
    private final Bank bank;
    private final CommandInput command;

    public SetAliasCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    @Override
    public void execute() {
        bank.getAliases().put(command.getAlias(), command.getAccount());
    }
}
