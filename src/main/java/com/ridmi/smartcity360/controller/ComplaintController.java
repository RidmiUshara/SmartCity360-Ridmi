package com.ridmi.smartcity360.controller;

import com.ridmi.smartcity360.complaint.ComplaintService;
import com.ridmi.smartcity360.complaint.Severity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComplaintController {
    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/complaints")
    public String showComplaints(Model model) {
        populate(model);
        return "complaints";
    }

    @PostMapping("/complaints")
    public String submitComplaint(@RequestParam String description,
                                  @RequestParam String location,
                                  @RequestParam Severity severity,
                                  Model model) {
        try {
            complaintService.submit(description, location, severity);
            return "redirect:/complaints";
        } catch (IllegalArgumentException ex) {
            populate(model);
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("description", description);
            model.addAttribute("location", location);
            model.addAttribute("selectedSeverity", severity);
            return "complaints";
        }
    }

    private void populate(Model model) {
        model.addAttribute("complaints", complaintService.getComplaints());
        model.addAttribute("severities", Severity.values());
    }
}
