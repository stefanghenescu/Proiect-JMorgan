package org.poo.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.accounts.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class responsible for generating a classic report.
 * Implements the ReportStrategy interface. This class is part of the Strategy design pattern.
 */
public class ClassicReport implements ReportStrategy {
    /**
     * Method responsible for generating a classic report.
     * If the account is not found, an error message is returned.
     * @param bank the bank that contains the account data
     * @param command the command input that contains information about the report
     * @return the classic report as a JSON object
     */
    @Override
    public ObjectNode generateReport(final Bank bank, final CommandInput command) {
        Account account;
        try {
            account = bank.getAccount(command.getAccount());
        } catch (NoSuchElementException e) {
            return JsonOutput.accountNotFound(command);
        }

        List<Transaction> transactions = new ArrayList<>();

        // Add transactions to the list and filter them by timestamp
        for (Transaction transaction : account.getTransactions()) {
            addTransaction(transaction, transactions, command);
        }

        return JsonOutput.writeClassicReport(command, account, transactions);
    }

    private void addTransaction(final Transaction transaction, final List<Transaction> transactions,
                               final CommandInput command) {
        if (transaction.getTimestamp() >= command.getStartTimestamp()
                && transaction.getTimestamp() <= command.getEndTimestamp()) {
            transactions.add(transaction);
        }
    }
}
