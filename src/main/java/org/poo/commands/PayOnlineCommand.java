package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.account.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

public class PayOnlineCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public PayOnlineCommand(final Bank bank, final CommandInput command, final ArrayNode output) {
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

        User cardOwnerUser = bank.getUser(command.getEmail());
        Account cardAccount = card.getOwner();
        if (cardOwnerUser == null || !cardOwnerUser.getAccounts().contains(cardAccount)) {
            output.add(JsonOutput.cardNotFound(command));
            return;
        }

        // convert in account currency
        double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(),
                cardAccount.getCurrency());
        double amount = command.getAmount() * exchangeRate;

        card.payOnline(amount, command);
    }
}
