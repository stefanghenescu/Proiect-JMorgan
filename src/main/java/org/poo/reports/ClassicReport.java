package org.poo.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.bank.SetupBank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

import java.util.ArrayList;
import java.util.List;

public class ClassicReport implements ReportStrategy {
    @Override
    public ObjectNode generateReport(SetupBank bank, CommandInput command) {
        Account account = bank.getAccounts().get(command.getAccount());
        if (account == null) {
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
