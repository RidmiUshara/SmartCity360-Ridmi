package com.ridmi.smartcity360.policy;

import java.util.Map;

/** Interpreter abstract expression. */
public interface Expression {
    boolean evaluate(Map<String, Double> context);
}
