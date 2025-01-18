package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

/**
 * Class responsible for printing all users.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class PrintUsersCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public PrintUsersCommand(final Bank bank, final CommandInput command, final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for printing all users.
     * The users are printed using the JsonOutput class.
     */
    @Override
    public void execute() {
        ObjectNode usersArray = JsonOutput.writeUsers(command, bank);
        output.add(usersArray);
    }
}
