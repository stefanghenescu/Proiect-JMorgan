package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

/**
 * Class responsible for setting an alias to an account.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class SetAliasCommand implements Command {
    private final Bank bank;
    private final CommandInput command;

    public SetAliasCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    /**
     * Method responsible for setting an alias to an account.
     * The alias is added to the bank's aliases map.
     */
    @Override
    public void execute() {
        // Alias is the key and account IBAN is the value
        bank.getAliases().put(command.getAlias(), command.getAccount());
    }
}
