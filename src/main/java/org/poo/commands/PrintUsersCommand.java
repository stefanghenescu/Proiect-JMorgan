package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

public class PrintUsersCommand implements Command {
    private Bank bank;
    private CommandInput command;
    private ArrayNode output;

    public PrintUsersCommand(Bank bank, CommandInput command, ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    @Override
    public void execute() {
        ObjectNode usersArray = JsonOutput.writeUsers(command, bank);
        output.add(usersArray);
    }
}
