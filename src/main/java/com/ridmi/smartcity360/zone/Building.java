
package com.ridmi.smartcity360.zone;

public class Building implements CityComponent {

    private final String name;
    private final int powerUsage;
    private boolean powerOn;

    public Building(String name, int powerUsage) {
        this.name = name;
        this.powerUsage = powerUsage;
        this.powerOn = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setPower(boolean on) {
        this.powerOn = on;
    }

    @Override
    public int getPowerUsage() {
        return powerOn ? powerUsage : 0;
    }

    @Override
    public int getActiveBuildings() {
        return powerOn ? 1 : 0;
    }

    public boolean isPowerOn() {
        return powerOn;
    }
}
