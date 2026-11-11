
package com.ridmi.smartcity360.zone;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ZoneService {

    private final ZoneGroup city;
    private final Map<String, CityComponent> components =
            new LinkedHashMap<>();

    private final List<ZoneRow> zoneRows =
            new ArrayList<>();

    public ZoneService() {

        city = new ZoneGroup("SmartCity360");
        register("city", city, "City", 0);

        // North District
        ZoneGroup north = new ZoneGroup("North District");
        city.add(north);
        register("north", north, "District", 1);

        ZoneGroup market = new ZoneGroup("Market Street");
        north.add(market);
        register("market", market, "Street", 2);

        addBuilding(market, "hospital",
                "City Hospital", 150, 3);
        addBuilding(market, "school",
                "Central School", 80, 3);

        ZoneGroup park = new ZoneGroup("Park Street");
        north.add(park);
        register("park", park, "Street", 2);

        addBuilding(park, "library",
                "Public Library", 40, 3);

        // South District
        ZoneGroup south = new ZoneGroup("South District");
        city.add(south);
        register("south", south, "District", 1);

        ZoneGroup lake = new ZoneGroup("Lake Road");
        south.add(lake);
        register("lake", lake, "Street", 2);

        addBuilding(lake, "mall",
                "Shopping Centre", 120, 3);
        addBuilding(lake, "office",
                "City Office", 50, 3);
    }

    private void register(String id, CityComponent component,
                          String type, int level) {

        components.put(id, component);
        zoneRows.add(new ZoneRow(
                id, component.getName(), type, level,
                component
        ));
    }

    private void addBuilding(ZoneGroup parent, String id,
                             String name, int usage, int level) {

        Building building = new Building(name, usage);
        parent.add(building);
        register(id, building, "Building", level);
    }

    public synchronized List<ZoneRow> getZones() {
        return List.copyOf(zoneRows);
    }

    public synchronized int getTotalUsage() {
        return city.getPowerUsage();
    }

    public synchronized int getActiveBuildings() {
        return city.getActiveBuildings();
    }

    public synchronized boolean setPower(String id, boolean on) {
        CityComponent component = components.get(id);

        if (component == null) {
            return false;
        }

        component.setPower(on);
        return true;
    }

    public record ZoneRow(
            String id,
            String name,
            String type,
            int level,
            CityComponent component
    ) {
        public int getUsage() {
            return component.getPowerUsage();
        }

        public int getActive() {
            return component.getActiveBuildings();
        }
    }
}
