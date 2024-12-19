package org.poo.commands;

import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.cards.CardOneTimeUse;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.NoSuchElementException;

public class CreateCardCommand implements Command {
    private final Bank bank;
    private final CommandInput command;

    public CreateCardCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    public void execute() {
        // get the account to add the card to
        Account account;
        try {
            account = bank.getAccount(command.getAccount());
        } catch (NoSuchElementException e) {
            return;
        }

        User user;
        try {
            user = bank.getUser(command.getEmail());
        } catch (NoSuchElementException e) {
            return;
        }

        if (!user.getAccounts().contains(account)) {
            return;
        }

        Card card;
        if (command.getCommand().equals("createOneTimeCard")) {
            card = new CardOneTimeUse(account, bank);
        } else {
            card = new Card(account);
        }

        // add the card to the account
        account.addCard(card);

        // add the card to the bank database
        bank.getCards().put(card.getNumber(), card);

        // transaction for later update()
        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                "New card created")
                .card(card.getNumber())
                .cardHolder(user.getEmail())
                .account(account.getIban())
                .build();

        user.addTransaction(transaction);
        account.addTransaction(transaction);
    }
}
