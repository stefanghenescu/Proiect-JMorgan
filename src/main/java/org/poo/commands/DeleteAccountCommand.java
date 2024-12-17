package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.account.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

public class DeleteAccountCommand implements Command {
    private Bank bank;
    private CommandInput command;
    private ArrayNode output;

    public DeleteAccountCommand(Bank bank, CommandInput command, ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    @Override
    public void execute() {
        String accountIBAN = bank.getAliases().get(command.getAccount());

        // get the account to delete
        Account account = bank.getAccount(accountIBAN);

        if (account == null) {
            return;
        }

        // get the user to delete the account from
        User user = bank.getUser(command.getEmail());

        if (user == null) {
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
