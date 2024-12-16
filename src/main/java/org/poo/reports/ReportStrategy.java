package org.poo.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Commerciant;
import org.poo.bank.SetupBank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.List;
import java.util.Map;

public interface ReportStrategy {
    ObjectNode generateReport(SetupBank bank, CommandInput command);
}
