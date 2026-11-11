package com.ridmi.smartcity360.light;

/** Immutable intrinsic configuration shared by many streetlights. */
public record LightConfiguration(String model, int wattage, String color) {
    public LightConfiguration {
        if (model == null || model.isBlank() || wattage <= 0 || color == null || color.isBlank()) {
            throw new IllegalArgumentException("A valid shared lamp configuration is required.");
        }
    }
}
