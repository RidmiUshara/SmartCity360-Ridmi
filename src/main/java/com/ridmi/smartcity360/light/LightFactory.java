package com.ridmi.smartcity360.light;

import java.util.HashMap;
import java.util.Map;

/** Flyweight factory: a single configuration object per unique set of intrinsic properties. */
public class LightFactory {
    private final Map<LightConfiguration, LightConfiguration> cache = new HashMap<>();

    public synchronized LightConfiguration getConfiguration(String model, int wattage, String color) {
        LightConfiguration key = new LightConfiguration(model, wattage, color);
        return cache.computeIfAbsent(key, unused -> key);
    }

    public synchronized int sharedConfigurationCount() {
        return cache.size();
    }
}
