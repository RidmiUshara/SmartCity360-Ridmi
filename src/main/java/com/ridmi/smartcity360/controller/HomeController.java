package com.ridmi.smartcity360.controller;

import com.ridmi.smartcity360.complaint.Complaint;
import com.ridmi.smartcity360.complaint.ComplaintService;
import com.ridmi.smartcity360.complaint.Severity;
import com.ridmi.smartcity360.light.LightService;
import com.ridmi.smartcity360.policy.PolicyService;
import com.ridmi.smartcity360.zone.ZoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private final ZoneService zoneService;
    private final ComplaintService complaintService;
    private final LightService lightService;
    private final PolicyService policyService;

    public HomeController(ZoneService zoneService, ComplaintService complaintService,
                          LightService lightService, PolicyService policyService) {
        this.zoneService = zoneService;
        this.complaintService = complaintService;
        this.lightService = lightService;
        this.policyService = policyService;
    }

    @GetMapping("/")
    public String home(Model model) {
        int totalUsage = zoneService.getTotalUsage();
        int activeBuildings = zoneService.getActiveBuildings();
        int totalBuildings = (int) zoneService.getZones().stream()
                .filter(row -> "Building".equals(row.type())).count();
        List<DistrictUsage> districts = zoneService.getZones().stream()
                .filter(row -> "District".equals(row.type()))
                .map(row -> new DistrictUsage(row.name(), row.getUsage(), row.getActive(),
                        totalUsage == 0 ? 0 : row.getUsage() * 100.0 / totalUsage))
                .toList();

        long activeLights = lightService.getPoweredLights();
        int totalLights = lightService.getTotalLights();
        long inactiveLights = totalLights - activeLights;
        double activePercent = totalLights == 0 ? 0 : activeLights * 100.0 / totalLights;
        List<Complaint> complaints = complaintService.getComplaints();
        List<SeverityCount> severities = new ArrayList<>();
        for (Severity severity : Severity.values()) {
            long count = complaints.stream().filter(c -> c.getSeverity() == severity).count();
            double percentage = complaints.isEmpty() ? 0 : count * 100.0 / complaints.size();
            severities.add(new SeverityCount(severity.name(), count, percentage));
        }

        model.addAttribute("totalUsage", totalUsage);
        model.addAttribute("activeBuildings", activeBuildings);
        model.addAttribute("totalBuildings", totalBuildings);
        model.addAttribute("districts", districts);
        model.addAttribute("activeLights", activeLights);
        model.addAttribute("inactiveLights", inactiveLights);
        model.addAttribute("totalLights", totalLights);
        model.addAttribute("activePercent", activePercent);
        model.addAttribute("sharedProfiles", lightService.getSharedProfiles());
        model.addAttribute("complaintCount", complaints.size());
        model.addAttribute("recentComplaints", complaints.stream().limit(4).toList());
        model.addAttribute("severities", severities);
        model.addAttribute("policyCount", policyService.getRules().size());
        // Coordinates are illustrative markers around Colombo, not actual asset locations.
        model.addAttribute("mapSites", zoneService.getZones().stream()
                .filter(row -> "Building".equals(row.type()))
                .map(row -> {
                    double[] point = switch (row.id()) {
                        case "hospital" -> new double[]{6.9316, 79.8577};
                        case "school" -> new double[]{6.9365, 79.8720};
                        case "library" -> new double[]{6.9220, 79.8619};
                        case "mall" -> new double[]{6.9140, 79.8508};
                        case "office" -> new double[]{6.9067, 79.8684};
                        default -> new double[]{6.9271, 79.8612};
                    };
                    return new MapSite(row.name(), point[0], point[1],
                            row.getActive() > 0, row.getUsage());
                }).toList());
        return "index";
    }

    public record DistrictUsage(String name, int usageKw, int activeBuildings, double percent) {}
    public record SeverityCount(String name, long count, double percent) {}
    public record MapSite(String name, double lat, double lng, boolean powered, int usage) {}
}
