package com.ridmi.smartcity360.policy;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class InterpreterPatternTest {
    @Test
    void evaluatesRulesAndOperatorPrecedence() {
        var parsed = PolicyParser.parse("WHEN noise > 70 OR occupancy > 80 AND hour >= 22 THEN ALERT");
        assertEquals("ALERT", parsed.action());
        assertTrue(parsed.condition().evaluate(Map.of("noise", 75.0)));
        assertFalse(parsed.condition().evaluate(Map.of("noise", 50.0, "occupancy", 90.0, "hour", 12.0)));
        var grouped = PolicyParser.parse("WHEN (noise > 70 OR occupancy > 80) AND NOT hour < 22 THEN LOG");
        assertFalse(grouped.condition().evaluate(Map.of("noise", 75.0, "hour", 11.0)));
        assertTrue(grouped.condition().evaluate(Map.of("noise", 75.0, "hour", 23.0)));
    }

    @Test
    void rejectsUnsupportedSyntaxAndAllowsUpdates() {
        assertThrows(IllegalArgumentException.class, () -> PolicyParser.parse("WHEN java.lang.System > 0 THEN ALERT"));
        assertThrows(IllegalArgumentException.class, () -> PolicyParser.parse("WHEN noise > 10 THEN DELETE"));
        PolicyService service = new PolicyService();
        var added = service.add("Sample", "WHEN noise >= 50 THEN ALERT");
        assertTrue(service.evaluate(Map.of("noise", 60.0)).get(3).matched());
        assertTrue(service.remove(added.id()));
        assertEquals(3, service.getRules().size());
    }
}
