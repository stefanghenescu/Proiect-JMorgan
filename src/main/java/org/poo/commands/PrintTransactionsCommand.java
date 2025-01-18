package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

/**
 * Class responsible for printing the transactions of a user.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class PrintTransactionsCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public PrintTransactionsCommand(final Bank bank, final CommandInput command,
                                    final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for printing the transactions of a user.
     * The transactions are printed using the JsonOutput class.
     * If the user is not found, the command will terminate without changes.
     */
    @Override
    public void execute() {
        User transactionsUser;
        try {
            transactionsUser = bank.getUser(command.getEmail());
        } catch (NoSuchElementException e) {
            return;
        }

        ObjectNode transactionsArray = JsonOutput.writeTransactions(command, transactionsUser);
        output.add(transactionsArray);
    }
}
