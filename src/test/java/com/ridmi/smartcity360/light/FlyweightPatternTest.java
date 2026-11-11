package com.ridmi.smartcity360.light;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlyweightPatternTest {
    @Test
    void sharesProfilesWithoutSharingPowerState() {
        LightService service = new LightService();
        assertEquals(10_000, service.getTotalLights());
        assertEquals(3, service.getSharedProfiles());
        assertEquals(10_000, service.getPoweredLights());
        assertTrue(service.sameConfiguration(1, 4));
        assertFalse(service.sameConfiguration(1, 2));
        assertTrue(service.toggle(1));
        assertEquals(9_999, service.getPoweredLights());
        assertFalse(service.getSampleLights().get(0).isOn());
        assertTrue(service.getSampleLights().get(3).isOn());
        assertFalse(service.toggle(-1));
    }
}
