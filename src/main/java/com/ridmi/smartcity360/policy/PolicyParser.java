package com.ridmi.smartcity360.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Bounded recursive-descent parser for educational SmartCity360 rules; never runs arbitrary Java. */
public final class PolicyParser {
    private static final Set<String> METRICS = Set.of("noise", "hour", "occupancy", "parkedMinutes");
    private static final Set<String> ACTIONS = Set.of("ALERT", "ISSUE_FINE", "LOG");
    private final List<String> tokens;
    private int index;
    private int nesting;

    private PolicyParser(String source) {
        if (source == null || source.isBlank() || source.length() > 256) {
            throw new IllegalArgumentException("Rule must contain 1–256 characters.");
        }
        tokens = tokenize(source);
        if (tokens.size() > 80) throw new IllegalArgumentException("Rule is too long.");
    }

    public static ParsedPolicy parse(String source) {
        PolicyParser parser = new PolicyParser(source);
        parser.expect("WHEN");
        Expression expression = parser.parseOr();
        parser.expect("THEN");
        String action = parser.next();
        if (!ACTIONS.contains(action)) throw new IllegalArgumentException("Unsupported rule action: " + action);
        if (parser.index != parser.tokens.size()) throw new IllegalArgumentException("Unexpected token after action.");
        return new ParsedPolicy(expression, action);
    }

    private Expression parseOr() {
        Expression left = parseAnd();
        while (accept("OR")) left = new OrExpression(left, parseAnd());
        return left;
    }

    private Expression parseAnd() {
        Expression left = parseUnary();
        while (accept("AND")) left = new AndExpression(left, parseUnary());
        return left;
    }

    private Expression parseUnary() {
        if (++nesting > 16) throw new IllegalArgumentException("Rule nesting is too deep.");
        try {
            if (accept("NOT")) return new NotExpression(parseUnary());
            if (accept("(")) {
                Expression expression = parseOr();
                expect(")");
                return expression;
            }
            String metric = next();
            if (!METRICS.contains(metric)) throw new IllegalArgumentException("Unknown sensor metric: " + metric);
            String operator = next();
            if (!Set.of(">", ">=", "<", "<=", "==").contains(operator)) {
                throw new IllegalArgumentException("Invalid comparison operator: " + operator);
            }
            String rawNumber = next();
            try {
                double value = Double.parseDouble(rawNumber);
                if (!Double.isFinite(value)) throw new NumberFormatException();
                return new ComparisonExpression(metric, operator, value);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Expected a finite numeric threshold.", ex);
            }
        } finally {
            nesting--;
        }
    }

    private boolean accept(String token) {
        if (index < tokens.size() && tokens.get(index).equals(token)) {
            index++;
            return true;
        }
        return false;
    }

    private void expect(String token) {
        if (!accept(token)) throw new IllegalArgumentException("Expected: " + token);
    }

    private String next() {
        if (index >= tokens.size()) throw new IllegalArgumentException("Rule ended unexpectedly.");
        return tokens.get(index++);
    }

    private static List<String> tokenize(String source) {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < source.length()) {
            char c = source.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }
            if (Character.isLetter(c)) {
                int start = i++;
                while (i < source.length() && Character.isLetter(source.charAt(i))) i++;
                tokens.add(source.substring(start, i));
                continue;
            }
            if (Character.isDigit(c) || c == '-' || c == '.') {
                int start = i++;
                while (i < source.length() && (Character.isDigit(source.charAt(i)) || source.charAt(i) == '.')) i++;
                tokens.add(source.substring(start, i));
                continue;
            }
            if (c == '(' || c == ')' || c == '<' || c == '>' || c == '=') {
                int start = i++;
                if (i < source.length() && source.charAt(i) == '=' && c != '(' && c != ')') i++;
                tokens.add(source.substring(start, i));
                continue;
            }
            throw new IllegalArgumentException("Unsupported character in rule.");
        }
        return tokens;
    }

    public record ParsedPolicy(Expression condition, String action) { }
}
