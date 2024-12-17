package org.poo.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.account.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ClassicReport implements ReportStrategy {
    @Override
    public ObjectNode generateReport(Bank bank, CommandInput command) {
        Account account;
        try {
            account = bank.getAccount(command.getAccount());
        } catch (NoSuchElementException e) {
            return JsonOutput.accountNotFound(command);
        }

        List<Transaction> transactions = new ArrayList<>();

        for (Transaction transaction : account.getTransactions()) {
            addTransaction(transaction, transactions, command);
        }

        return JsonOutput.writeClassicReport(command, account, transactions);
    }

    private void addTransaction(Transaction transaction, List<Transaction> transactions,
                               CommandInput command) {
        if (transaction.getTimestamp() >= command.getStartTimestamp()
                && transaction.getTimestamp() <= command.getEndTimestamp()) {
            transactions.add(transaction);
        }
    }
}
