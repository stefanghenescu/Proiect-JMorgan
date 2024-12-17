package org.poo.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public interface ReportStrategy {
    ObjectNode generateReport(Bank bank, CommandInput command);
}
