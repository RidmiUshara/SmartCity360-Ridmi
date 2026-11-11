package com.ridmi.smartcity360.controller;

import com.ridmi.smartcity360.light.LightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LightController {
    private final LightService lightService;

    public LightController(LightService lightService) { this.lightService = lightService; }

    @GetMapping("/lights")
    public String showLights(Model model) {
        model.addAttribute("totalLights", lightService.getTotalLights());
        model.addAttribute("sharedProfiles", lightService.getSharedProfiles());
        model.addAttribute("poweredLights", lightService.getPoweredLights());
        model.addAttribute("lights", lightService.getSampleLights());
        return "lights";
    }

    @PostMapping("/lights/{id}/toggle")
    public String toggle(@PathVariable int id) {
        lightService.toggle(id);
        return "redirect:/lights";
    }
}
