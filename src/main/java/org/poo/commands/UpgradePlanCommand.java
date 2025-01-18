package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

import java.util.NoSuchElementException;

public class UpgradePlanCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public UpgradePlanCommand(final Bank bank, final CommandInput command, final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

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

        String message = changePlanUser.upgradePlan(command.getNewPlanType(), changePlanAccount,
                bank);

        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                message)
                .accountIBAN(command.getAccount())
                .newPlanType(command.getNewPlanType())
                .build();

        changePlanUser.addTransaction(transaction);
        changePlanAccount.addTransaction(transaction);
    }
}
