package org.poo.commands;

import org.poo.bank.account.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.NoSuchElementException;

public class DeleteCardCommand implements Command {
    private Bank bank;
    private CommandInput command;

    public DeleteCardCommand(Bank bank, CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

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

        // transaction for later update()
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
