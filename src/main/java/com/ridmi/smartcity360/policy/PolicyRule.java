package com.ridmi.smartcity360.policy;

import java.util.Map;

public record PolicyRule(int id, String name, String source, Expression condition, String action) {
    public boolean matches(Map<String, Double> context) {
        return condition.evaluate(context);
    }
}
