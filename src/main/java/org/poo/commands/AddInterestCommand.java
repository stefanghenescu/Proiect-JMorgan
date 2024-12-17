package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Account;
import org.poo.account.SavingsAccount;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

public class AddInterestCommand implements Command {
    private Bank bank;
    private CommandInput command;
    private ArrayNode output;

    public AddInterestCommand(Bank bank, CommandInput command, ArrayNode output) {
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

        double interestRate = ((SavingsAccount) account).getInterestRate();
        double interest = account.getBalance() * interestRate;

        account.addFunds(interest);
    }
}
