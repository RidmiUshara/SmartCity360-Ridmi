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
import java.util.Arrays;
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
        return "index";
    }

    public record DistrictUsage(String name, int usageKw, int activeBuildings, double percent) {}
    public record SeverityCount(String name, long count, double percent) {}
}
