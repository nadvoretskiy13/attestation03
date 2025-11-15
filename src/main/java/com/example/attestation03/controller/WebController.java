package com.example.attestation03.controller;

import com.example.attestation03.service.DoctorService;
import com.example.attestation03.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    public WebController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/patients")
    public String patients(Model model) {
        model.addAttribute("patients", patientService.listAll());
        return "patients";
    }

    @GetMapping("/doctors")
    public String doctors(Model model) {
        model.addAttribute("doctors", doctorService.listAll());
        return "doctors";
    }
}
