package org.poo.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.SetupBank;
import org.poo.fileio.CommandInput;

public interface ReportStrategy {
    ObjectNode generateReport(SetupBank bank, CommandInput command);
}
