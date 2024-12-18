package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.account.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

public class DeleteAccountCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public DeleteAccountCommand(final Bank bank, final CommandInput command,
                                final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    @Override
    public void execute() {
        String accountIBAN = bank.getAliases().get(command.getAccount());

        // get the account to delete
        Account account;
        try {
            account = bank.getAccount(accountIBAN);
        } catch (NoSuchElementException e) {
            return;
        }

        // get the user to delete the account from
        User user;
        try {
            user = bank.getUser(command.getEmail());
        } catch (NoSuchElementException e) {
            return;
        }

        // delete the account from the user
        if (user.deleteAccount(account)) {
            // delete the account from the bank database
            bank.getAccounts().remove(account.getIban());
        } else {
            // update transactions with an error message
            Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                    "Account couldn't be deleted - there are funds remaining")
                    .build();

            user.addTransaction(transaction);
        }

        output.add(JsonOutput.eraseAccount(command, account));
    }
}
