package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

public final class UpgradePlanCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public UpgradePlanCommand(final Bank bank, final CommandInput command, final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for upgrading the plan of a specific user.
     */
    @Override
    public void execute() {
        Account changePlanAccount;
        try {
            changePlanAccount = bank.getAccount(command.getAccount());
        } catch (NoSuchElementException e) {
            output.add(JsonOutput.accountNotFound(command));
            return;
        }

        User changePlanUser = changePlanAccount.getOwner();

        // try to upgrade the plan of the user
        String error = changePlanUser.upgradePlan(command.getNewPlanType(), changePlanAccount,
                bank);

        // create a transaction
        // if there is an error, the transaction is an error transaction
        // otherwise, the transaction is a successful upgrade plan transaction
        Transaction transaction;
        if (error != null) {
            transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                    error)
                    .build();
        } else {
            transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                    "Upgrade plan")
                    .accountIBAN(command.getAccount())
                    .newPlanType(command.getNewPlanType())
                    .build();
        }

        changePlanUser.addTransaction(transaction);
        changePlanAccount.addTransaction(transaction);
    }
}
