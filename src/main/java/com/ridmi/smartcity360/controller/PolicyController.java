package com.ridmi.smartcity360.controller;

import com.ridmi.smartcity360.policy.PolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PolicyController {
    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) { this.policyService = policyService; }

    @GetMapping("/policies")
    public String showPolicies(Model model) {
        model.addAttribute("rules", policyService.getRules());
        return "policies";
    }

    @PostMapping("/policies/add")
    public String addRule(@RequestParam String name, @RequestParam String source, Model model) {
        try {
            policyService.add(name, source);
            return "redirect:/policies";
        } catch (IllegalArgumentException e) {
            model.addAttribute("rules", policyService.getRules());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("name", name);
            model.addAttribute("source", source);
            return "policies";
        }
    }

    @PostMapping("/policies/evaluate")
    public String evaluate(@RequestParam double noise, @RequestParam double hour,
                           @RequestParam double occupancy, @RequestParam double parkedMinutes,
                           Model model) {
        model.addAttribute("rules", policyService.getRules());
        model.addAttribute("results", policyService.evaluate(Map.of(
                "noise", noise, "hour", hour, "occupancy", occupancy, "parkedMinutes", parkedMinutes
        )));
        model.addAttribute("noise", noise);
        model.addAttribute("hour", hour);
        model.addAttribute("occupancy", occupancy);
        model.addAttribute("parkedMinutes", parkedMinutes);
        return "policies";
    }

    @PostMapping("/policies/remove")
    public String remove(@RequestParam int id) {
        policyService.remove(id);
        return "redirect:/policies";
    }
}
