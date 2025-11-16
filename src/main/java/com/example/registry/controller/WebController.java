package com.example.registry.controller;

import com.example.registry.model.Patient;
import com.example.registry.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final PatientService patientService;

    public WebController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping({"/", "/web/home"})
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password) {
        if (username != null && !username.isBlank()) {
            return "redirect:/patients";
        }
        return "login";
    }

    @GetMapping("/patients")
    public String patients(Model model) {
        model.addAttribute("patients", patientService.findActive());
        model.addAttribute("newPatient", new Patient());
        return "patients";
    }

    @PostMapping("/patients")
    public String add(@ModelAttribute("newPatient") Patient patient) {
        patientService.save(patient);
        return "redirect:/patients";
    }

    @PostMapping("/patients/delete/{id}")
    public String delete(@PathVariable Long id) {
        patientService.softDelete(id);
        return "redirect:/patients";
    }
}
