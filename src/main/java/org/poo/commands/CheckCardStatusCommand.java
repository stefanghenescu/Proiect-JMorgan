package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.cards.Card;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

public class CheckCardStatusCommand implements Command {
    private Bank bank;
    private CommandInput command;
    private ArrayNode output;

    public CheckCardStatusCommand(final Bank bank, final CommandInput command,
                                  final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    @Override
    public void execute() {
        Card card;
        try {
            card = bank.getCard(command.getCardNumber());
        } catch (NoSuchElementException e) {
            output.add(JsonOutput.cardNotFound(command));
            return;
        }

        card.check(command.getTimestamp());
    }
}
