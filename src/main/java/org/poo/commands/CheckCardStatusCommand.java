package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.cards.Card;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

/**
 * Class responsible for checking the status of a card.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class CheckCardStatusCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public CheckCardStatusCommand(final Bank bank, final CommandInput command,
                                  final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for checking the status of a card.
     * If the card is not found, an error message is added to the output.
     * If the card is found, the card is checked.
     */
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
