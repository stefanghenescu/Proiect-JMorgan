package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.account.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

public class PayOnlineCommand implements Command {
    private Bank bank;
    private CommandInput command;
    private ArrayNode output;

    public PayOnlineCommand(Bank bank, CommandInput command, ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    @Override
    public void execute() {
        Card card = bank.getCard(command.getCardNumber());
        User cardOwnerUser = bank.getUser(command.getEmail());

        if (card == null) {
            output.add(JsonOutput.cardNotFound(command));
            return;
        }

        Account cardAccount = card.getOwner();
        if (cardOwnerUser == null || !cardOwnerUser.getAccounts().contains(cardAccount)) {
            output.add(JsonOutput.cardNotFound(command));
            return;
        }

        // convert in account currency
        double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(),
                cardAccount.getCurrency());
        double amount = command.getAmount() * exchangeRate;

        boolean paidWithOneTimeCard = card.payOnline(amount, command.getTimestamp(),
                command.getCommerciant());

        if (paidWithOneTimeCard) {
            // delete the card
            DeleteCardCommand deleteCardCommand = new DeleteCardCommand(bank, command);
            deleteCardCommand.execute();

            // create a new one-time card
            command.setCommand("createOneTimeCard");
            command.setAccount(cardAccount.getIban());

            CreateCardCommand createCardCommand = new CreateCardCommand(bank, command);
            createCardCommand.execute();
        }
    }
}
