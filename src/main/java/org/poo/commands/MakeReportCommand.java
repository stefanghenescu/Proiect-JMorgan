package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.reports.ReportFactory;
import org.poo.reports.ReportStrategy;

public class MakeReportCommand implements Command {
    private Bank bank;
    private CommandInput command;
    private ArrayNode output;

    public MakeReportCommand(Bank bank, CommandInput command, ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    @Override
    public void execute() {
        ReportStrategy reportStrategy = ReportFactory.getReportType(command.getCommand());
        reportStrategy.generateReport(bank, command);

        ObjectNode report = reportStrategy.generateReport(bank, command);
        output.add(report);
    }
}
