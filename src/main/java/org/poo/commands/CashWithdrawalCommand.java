package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

public final class CashWithdrawalCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public CashWithdrawalCommand(final Bank bank, final CommandInput command,
                                 final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for withdrawing cash from an ATM.
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

        // get the user if it exists
        User cardUser;
        try {
            cardUser = bank.getUser(command.getEmail());
        } catch (NoSuchElementException e) {
            output.add(JsonOutput.userNotFound(command));
            return;
        }

        Account cardAccount = card.getOwner();
        // get the rate from RON to the account currency as command amount is in RON
        double rateToAccountCurrency = bank.getExchangeRates().getRate("RON",
                                                                        cardAccount.getCurrency());
        double amount = command.getAmount() * rateToAccountCurrency;

        // withdraw the amount in the correct rate from the account if possible
        if (card.atmWithdraw(amount, command)) {
            // calculate the commission and withdraw it from the account
            double commission = cardUser.getPlanStrategy().calculateCommission(amount, bank,
                                                                        cardAccount.getCurrency());
            cardAccount.withdraw(commission);
        }
    }
}
