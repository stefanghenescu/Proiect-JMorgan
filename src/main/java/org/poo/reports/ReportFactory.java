package org.poo.reports;

/**
 * Factory class responsible for creating different types of reports.
 */
public final class ReportFactory {
    // Prevent instantiation of the utility class
    private ReportFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Get the report type based on the command input.
     * @param reportType the type of report to be created
     * @return an instance of the appropriate report type
     * @throws IllegalArgumentException if the report type is invalid
     */
    public static ReportStrategy getReportType(final String reportType) {
        return switch (reportType) {
            case "report" -> new ClassicReport();
            case "spendingsReport" -> new SpendingReport();
            default -> throw new IllegalArgumentException("Invalid report type: " + reportType);
        };
    }
}
