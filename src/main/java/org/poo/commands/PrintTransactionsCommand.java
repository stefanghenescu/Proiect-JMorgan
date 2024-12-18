package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

public class PrintTransactionsCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public PrintTransactionsCommand(final Bank bank, final CommandInput command,
                                    final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    @Override
    public void execute() {
        User transactionsUser = bank.getUser(command.getEmail());
        ObjectNode transactionsArray = JsonOutput.writeTransactions(command, transactionsUser);
        output.add(transactionsArray);
    }
}
