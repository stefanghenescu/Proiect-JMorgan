package org.poo.reports;

public class ReportFactory {
    public static ReportStrategy getReportType(String reportType) {
        return switch (reportType) {
            case "report" -> new ClassicReport();
            case "spendingsReport" -> new SpendingReport();
            default -> throw new IllegalArgumentException("Invalid report type: " + reportType);
        };
    }
}
