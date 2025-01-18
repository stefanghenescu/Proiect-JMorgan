package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.reports.ReportFactory;
import org.poo.reports.ReportStrategy;

/**
 * Class responsible for generating a report.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class MakeReportCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public MakeReportCommand(final Bank bank, final CommandInput command, final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for generating a report.
     * The report is generated using the ReportFactory class.
     */
    @Override
    public void execute() {
        // Get the report type from the command
        ReportStrategy reportStrategy = ReportFactory.getReportType(command.getCommand());

        ObjectNode report = reportStrategy.generateReport(bank, command);
        output.add(report);
    }
}
