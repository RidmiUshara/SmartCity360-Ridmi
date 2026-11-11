package com.ridmi.smartcity360.policy;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PolicyService {
    private final List<PolicyRule> rules = new ArrayList<>();
    private int nextId = 1;

    public PolicyService() {
        add("Quiet hours", "WHEN noise > 70 AND hour >= 22 THEN ALERT");
        add("Parking limit", "WHEN parkedMinutes > 120 THEN ISSUE_FINE");
        add("Crowded space", "WHEN occupancy >= 90 THEN LOG");
    }

    public synchronized PolicyRule add(String name, String source) {
        if (name == null || name.isBlank() || name.length() > 80) {
            throw new IllegalArgumentException("Policy name must contain 1 to 80 characters.");
        }
        PolicyParser.ParsedPolicy parsed = PolicyParser.parse(source);
        PolicyRule rule = new PolicyRule(nextId++, name.trim(), source.trim(), parsed.condition(), parsed.action());
        rules.add(rule);
        return rule;
    }

    public synchronized List<PolicyRule> getRules() { return List.copyOf(rules); }

    public synchronized List<PolicyResult> evaluate(Map<String, Double> context) {
        List<PolicyResult> result = new ArrayList<>();
        for (PolicyRule rule : rules) {
            result.add(new PolicyResult(rule, rule.matches(context)));
        }
        return List.copyOf(result);
    }

    public synchronized boolean remove(int id) {
        return rules.removeIf(rule -> rule.id() == id);
    }

    public record PolicyResult(PolicyRule rule, boolean matched) { }
}
