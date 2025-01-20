package org.poo.bank.plans;

public class PlanFactory {
    public static PlanStrategy getPlanStrategy(String planType) {
        return switch (planType.toLowerCase()) {
            case "student" -> new StudentPlan();
            case "standard" -> new StandardPlan();
            case "silver" -> new SilverPlan();
            case "gold" -> new GoldPlan();
            default -> throw new IllegalArgumentException("Unknown plan type: " + planType);
        };
    }
}
