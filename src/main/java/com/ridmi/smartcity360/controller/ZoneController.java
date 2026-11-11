
package com.ridmi.smartcity360.controller;

import com.ridmi.smartcity360.zone.ZoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping("/zones")
    public String showZones(Model model) {

        model.addAttribute("zones", zoneService.getZones());

        model.addAttribute("totalUsage",
                zoneService.getTotalUsage());

        model.addAttribute("activeBuildings",
                zoneService.getActiveBuildings());

        return "zones";
    }

    @PostMapping("/zones/{id}/power")
    public String changePower(
            @PathVariable String id,
            @RequestParam boolean on) {

        zoneService.setPower(id, on);

        return "redirect:/zones";
    }
}
