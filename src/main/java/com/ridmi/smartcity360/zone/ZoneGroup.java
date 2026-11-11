
package com.ridmi.smartcity360.zone;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ZoneGroup implements CityComponent {

    private final String name;
    private final List<CityComponent> children;

    public ZoneGroup(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void add(CityComponent component) {
        children.add(Objects.requireNonNull(component));
    }

    public List<CityComponent> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setPower(boolean on) {
        for (CityComponent child : children) {
            child.setPower(on);
        }
    }

    @Override
    public int getPowerUsage() {
        int total = 0;

        for (CityComponent child : children) {
            total += child.getPowerUsage();
        }

        return total;
    }

    @Override
    public int getActiveBuildings() {
        int count = 0;

        for (CityComponent child : children) {
            count += child.getActiveBuildings();
        }

        return count;
    }
}
