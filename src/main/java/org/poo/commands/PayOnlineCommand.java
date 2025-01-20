package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.Bank;
import org.poo.bank.commerciants.Commerciant;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

/**
 * Class responsible for paying online with a card.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class PayOnlineCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public PayOnlineCommand(final Bank bank, final CommandInput command, final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for paying online with a card.
     * If the card is not found, an error message is added to the output.
     */
    @Override
    public void execute() {
        // get the card if it exists
        Card card;
        try {
            card = bank.getCard(command.getCardNumber());
        } catch (NoSuchElementException e) {
            output.add(JsonOutput.cardNotFound(command));
            return;
        }

        Account cardAccount = card.getOwner();
        User user = cardAccount.getOwner();

        // commerciant is needed for cashback
        Commerciant commerciant = bank.getCommerciantByName(command.getCommerciant());

        // convert in account currency
        double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(),
                                                                cardAccount.getCurrency());
        double amount = command.getAmount() * exchangeRate;

        // calculate commission
        double commission = user.getPlanStrategy().calculateCommission(amount, bank,
                                                                        cardAccount.getCurrency());
        // pay online, withdraw commission and apply cashback
        if (card.payOnline(amount, command)) {
            cardAccount.withdraw(commission);
            commerciant.getCashbackStrategy().cashback(amount, cardAccount, bank);
        }
    }
}
