package com.ridmi.smartcity360.light;

/** Extrinsic pole identity, location, and on/off state stay independent. */
public class StreetLight {
    private final int id;
    private final String location;
    private final LightConfiguration configuration;
    private boolean on = true;

    public StreetLight(int id, String location, LightConfiguration configuration) {
        this.id = id;
        this.location = location;
        this.configuration = configuration;
    }

    public int getId() { return id; }
    public String getLocation() { return location; }
    public LightConfiguration getConfiguration() { return configuration; }
    public boolean isOn() { return on; }
    public void setOn(boolean on) { this.on = on; }
}
