
package com.ridmi.smartcity360.zone;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompositePatternTest {

    @Test
    void testPowerManagement() {

        Building hospital = new Building("Hospital", 150);
        Building school = new Building("School", 80);

        ZoneGroup street = new ZoneGroup("Main Street");
        street.add(hospital);
        street.add(school);

        ZoneGroup district = new ZoneGroup("Colombo District");
        district.add(street);

        assertEquals(230, district.getPowerUsage());
        assertEquals(2, district.getActiveBuildings());

        district.setPower(false);

        assertEquals(0, district.getPowerUsage());
        assertEquals(0, district.getActiveBuildings());

        district.setPower(true);

        assertEquals(230, district.getPowerUsage());
        assertEquals(2, district.getActiveBuildings());
    }
}
