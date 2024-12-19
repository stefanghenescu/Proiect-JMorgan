package org.poo.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

/**
 * Interface responsible for generating a report based on the strategy pattern and the report type.
 */
public interface ReportStrategy {
    /**
     * Method responsible for generating a report based on its type.
     * @param bank the bank that contains the account data
     * @param command the command input that contains information about the report
     * @return the report as a JSON object
     */
    ObjectNode generateReport(Bank bank, CommandInput command);
}
