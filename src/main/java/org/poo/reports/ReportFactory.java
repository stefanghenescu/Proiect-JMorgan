package org.poo.reports;

public class ReportFactory {
    private ReportFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static ReportStrategy getReportType(final String reportType) {
        return switch (reportType) {
            case "report" -> new ClassicReport();
            case "spendingsReport" -> new SpendingReport();
            default -> throw new IllegalArgumentException("Invalid report type: " + reportType);
        };
    }
}
