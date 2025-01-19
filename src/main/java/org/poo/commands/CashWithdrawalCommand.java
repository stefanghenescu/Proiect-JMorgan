package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

public class CashWithdrawalCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public CashWithdrawalCommand(final Bank bank, final CommandInput command, final ArrayNode output){
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

        User cardUser;
        try {
            cardUser = bank.getUser(command.getEmail());
        } catch (NoSuchElementException e) {
            output.add(JsonOutput.userNotFound(command));
            return;
        }

        Account cardAccount = card.getOwner();
        double rateToAccountCurrency = bank.getExchangeRates().getRate("RON", cardAccount.getCurrency());
        double amount = command.getAmount() * rateToAccountCurrency;

        if (card.atmWithdraw(amount, command)) {
            double commission = cardUser.getPlanStrategy().calculateCommission(amount, bank,
                                                                               cardAccount.getCurrency());
            cardAccount.withdraw(commission);
        }

    }
}
