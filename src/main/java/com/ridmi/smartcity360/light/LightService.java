package com.ridmi.smartcity360.light;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class LightService {
    private final LightFactory factory = new LightFactory();
    private final List<StreetLight> lights = new ArrayList<>();

    public LightService() {
        for (int i = 1; i <= 10_000; i++) {
            LightConfiguration configuration = switch (i % 3) {
                case 0 -> factory.getConfiguration("Eco-LED", 40, "Warm White");
                case 1 -> factory.getConfiguration("Road-LED", 70, "Cool White");
                default -> factory.getConfiguration("Park-LED", 30, "Neutral White");
            };
            lights.add(new StreetLight(i, "Zone " + ((i - 1) / 500 + 1) + " / Pole " + i, configuration));
        }
    }

    public synchronized int getTotalLights() { return lights.size(); }
    public synchronized int getSharedProfiles() { return factory.sharedConfigurationCount(); }
    public synchronized long getPoweredLights() { return lights.stream().filter(StreetLight::isOn).count(); }

    public synchronized List<StreetLight> getSampleLights() {
        return List.copyOf(lights.subList(0, Math.min(15, lights.size())));
    }

    public synchronized boolean toggle(int id) {
        if (id < 1 || id > lights.size()) return false;
        StreetLight light = lights.get(id - 1);
        light.setOn(!light.isOn());
        return true;
    }

    public synchronized boolean sameConfiguration(int idA, int idB) {
        return lights.get(idA - 1).getConfiguration() == lights.get(idB - 1).getConfiguration();
    }
}
