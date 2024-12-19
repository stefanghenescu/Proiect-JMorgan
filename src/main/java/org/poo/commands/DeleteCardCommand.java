package org.poo.commands;

import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.NoSuchElementException;

/**
 * Class that represents the command to delete a card.
 * This class implements the Command interface as part of the Command design pattern.
 */
public final class DeleteCardCommand implements Command {
    private final Bank bank;
    private final CommandInput command;

    public DeleteCardCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    /**
     * This method executes the process of deleting a card.
     * If the card cannot be found, the command will terminate without changes.
     */
    @Override
    public void execute() {
        // get the card to delete
        Card card;
        try {
            card = bank.getCard(command.getCardNumber());
        } catch (NoSuchElementException e) {
            return;
        }

        // delete the card from the account
        Account ownerAccount = card.getOwner();
        ownerAccount.deleteCard(card);

        // delete the card from the bank database
        bank.getCards().remove(card.getNumber());

        // transaction for card deletion
        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                "The card has been destroyed")
                .card(card.getNumber())
                .cardHolder(ownerAccount.getOwner().getEmail())
                .account(ownerAccount.getIban())
                .build();

        ownerAccount.getOwner().addTransaction(transaction);
        ownerAccount.addTransaction(transaction);
    }
}
