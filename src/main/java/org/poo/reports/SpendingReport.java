package org.poo.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.bank.Commerciant;
import org.poo.bank.SetupBank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

import java.util.*;

public class SpendingReport implements ReportStrategy {
    @Override
    public ObjectNode generateReport(SetupBank bank, CommandInput command) {
        Account account = bank.getAccounts().get(command.getAccount());

        if (account == null) {
            return JsonOutput.accountNotFound(command);
        }

        if (account.getAccountType().equals("savings")) {
            return JsonOutput.accountNotEligible(command);
        }

        List<Transaction> transactions = new ArrayList<>();

        // use TreeMap to sort the commerciants by name
        Map<String, Commerciant> commerciants = new TreeMap<>();

        for (Transaction transaction : account.getTransactions()) {
            if (transaction.getTimestamp() >= command.getStartTimestamp()
                && transaction.getTimestamp() <= command.getEndTimestamp()
                && transaction.getCommerciant() != null) {
                // add the transaction to the list of transactions for the report
                transactions.add(transaction);

                // check if the commerciant is already in the map of commerciants
                // update the amount of money received by the commerciant
                if (commerciants.containsKey(transaction.getCommerciant())) {
                    Commerciant commerciant = commerciants.get(transaction.getCommerciant());
                    commerciant.receiveMoney(transaction.getAmount());
                } else {
                    Commerciant commerciant = new Commerciant(transaction.getCommerciant());
                    commerciant.receiveMoney(transaction.getAmount());
                    commerciants.put(transaction.getCommerciant(), commerciant);
                }
            }
        }
        return JsonOutput.writeSpendingReport(command, account, transactions, commerciants);
    }
}
