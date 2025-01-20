package org.poo.bank.plans;

/**
 * Factory class to get a user's plan.
 */
public final class PlanFactory {
    /**
     * Private constructor to prevent instantiation.
     * @throws UnsupportedOperationException if the constructor is called
     */
    private PlanFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Get the plan for a user based on the plan type.
     * @param planType the plan type
     * @return the plan
     */
    public static PlanStrategy getPlanStrategy(final String planType) {
        return switch (planType.toLowerCase()) {
            case "student" -> new StudentPlan();
            case "standard" -> new StandardPlan();
            case "silver" -> new SilverPlan();
            case "gold" -> new GoldPlan();
            default -> throw new IllegalArgumentException("Unknown plan type: " + planType);
        };
    }
}
