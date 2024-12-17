package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Card;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

public class CheckCardStatusCommand implements Command {
    private Bank bank;
    private CommandInput command;
    private ArrayNode output;

    public CheckCardStatusCommand(Bank bank, CommandInput command, ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    @Override
    public void execute() {
        Card card = Card.getCard(bank, command.getCardNumber());

        if (card == null) {
            output.add(JsonOutput.cardNotFound(command));
            return;
        }

        card.check(command.getTimestamp());
    }
}
