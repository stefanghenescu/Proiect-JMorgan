package org.poo.commands;

import org.poo.bank.account.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.cards.CardOneTimeUse;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

public class CreateCardCommand implements Command {
    private Bank bank;
    private CommandInput command;

    public CreateCardCommand(Bank bank, CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    public void execute() {
        // get the account to add the card to
        Account account = bank.getAccount(command.getAccount());

        User user = bank.getUser(command.getEmail());
        if (user == null)
            return;

        if (!user.getAccounts().contains(account)) {
            // update() transactions with an error message
            return;
            //throw new IllegalArgumentException("User does not own the account");
        }

        Card card;
        if (command.getCommand().equals("createOneTimeCard")) {
            card = new CardOneTimeUse(account);
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
