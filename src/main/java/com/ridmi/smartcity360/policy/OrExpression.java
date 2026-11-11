package com.ridmi.smartcity360.policy;

import java.util.Map;

public record OrExpression(Expression left, Expression right) implements Expression {
    @Override
    public boolean evaluate(Map<String, Double> context) {
        return left.evaluate(context) || right.evaluate(context);
    }
}
