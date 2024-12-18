package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.account.Account;
import org.poo.bank.account.SavingsAccount;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

public class ChangeInterestRateCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public ChangeInterestRateCommand(final Bank bank, final CommandInput command,
                                     final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    @Override
    public void execute() {
        Account account = bank.getAccounts().get(command.getAccount());
        if (!account.getAccountType().equals("savings")) {
            output.add(JsonOutput.writeErrorSavingAccount(command));
            return;
        }

        ((SavingsAccount) account).setInterestRate(command.getInterestRate());

        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                "Interest rate of the account changed to " + command.getInterestRate())
                .build();

        account.getOwner().addTransaction(transaction);
        account.addTransaction(transaction);
    }
}
