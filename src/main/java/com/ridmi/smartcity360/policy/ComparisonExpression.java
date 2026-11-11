package com.ridmi.smartcity360.policy;

import java.util.Map;

/** Terminal expression that compares one sensor value with a numeric threshold. */
public record ComparisonExpression(String metric, String operator, double threshold) implements Expression {
    @Override
    public boolean evaluate(Map<String, Double> context) {
        Double value = context.get(metric);
        if (value == null) return false;
        return switch (operator) {
            case ">" -> value > threshold;
            case ">=" -> value >= threshold;
            case "<" -> value < threshold;
            case "<=" -> value <= threshold;
            case "==" -> Double.compare(value, threshold) == 0;
            default -> throw new IllegalArgumentException("Unknown comparison operator");
        };
    }
}
