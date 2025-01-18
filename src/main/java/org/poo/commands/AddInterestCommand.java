package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.accounts.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

import java.util.NoSuchElementException;

/**
 * Class responsible for adding interest to a savings account.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class AddInterestCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public AddInterestCommand(final Bank bank, final CommandInput command, final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for adding interest to a savings account.
     * If the account is not a savings account, an error message is added to the output.
     * If the interest rate was added, a transaction is created.
     */
    @Override
    public void execute() {
        Account account;
        try {
            account = bank.getAccount(command.getAccount());
        } catch (NoSuchElementException e) {
            return;
        }

        account.addInterestRate(command, output);
    }
}
