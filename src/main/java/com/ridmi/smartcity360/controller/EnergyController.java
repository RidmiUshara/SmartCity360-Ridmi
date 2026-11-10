package com.ridmi.smartcity360.controller;

import com.ridmi.smartcity360.stream.SecurityPipeline;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Web UI for Part A; the actual Decorator pattern lives in the stream package. */
@Controller
public class EnergyController {
    private final SecurityPipeline pipeline = new SecurityPipeline();

    @GetMapping("/energy")
    public String page(Model model) {
        model.addAttribute("reading", "Meter 101 | Usage: 250 kWh | 2026-11-10");
        model.addAttribute("compress", true);
        model.addAttribute("encrypt", true);
        model.addAttribute("authenticate", true);
        return "energy";
    }

    @PostMapping("/energy/process")
    public String process(@RequestParam String reading,
                          @RequestParam(defaultValue = "false") boolean compress,
                          @RequestParam(defaultValue = "false") boolean encrypt,
                          @RequestParam(defaultValue = "false") boolean authenticate,
                          Model model) {
        model.addAttribute("reading", reading);
        model.addAttribute("compress", compress);
        model.addAttribute("encrypt", encrypt);
        model.addAttribute("authenticate", authenticate);
        try {
            SecurityPipeline.Result result = pipeline.demonstrate(
                    reading, compress, encrypt, authenticate);
            model.addAttribute("encoded", result.encodedBase64());
            model.addAttribute("decoded", result.decoded());
            model.addAttribute("inputBytes", result.inputBytes());
            model.addAttribute("outputBytes", result.outputBytes());
            model.addAttribute("tamperRejected", result.tamperRejected());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "energy";
    }
}
