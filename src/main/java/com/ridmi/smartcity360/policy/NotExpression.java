package com.ridmi.smartcity360.policy;

import java.util.Map;

public record NotExpression(Expression inner) implements Expression {
    @Override
    public boolean evaluate(Map<String, Double> context) {
        return !inner.evaluate(context);
    }
}
