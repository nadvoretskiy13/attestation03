package com.example.reception.controller;

import com.example.reception.dto.PatientDto;
import com.example.reception.exception.PatientAlreadyExistsException;
import com.example.reception.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientWebController {

    private final PatientService patientService;

    @GetMapping
    public String getPatients(Model model) {
        var patients = patientService.findAll();
        model.addAttribute("patients", patients);
        return "index";
    }

    @GetMapping("/add")
    public String addPatient(Model model) {
        model.addAttribute("patient", PatientDto.builder().build());
        model.addAttribute("mappingUri", "add");
        model.addAttribute("action", "reception.action.add");
        return "patient";
    }

    @PostMapping("/add")
    public String addPatient(@Valid @ModelAttribute("patient") PatientDto patientDto,
                             BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                patientService.create(patientDto);
                return "redirect:/patients";
            } catch (PatientAlreadyExistsException e) {
                bindingResult.rejectValue("email", e.getMessage());
            }
        }

        model.addAttribute("mappingUri", "add");
        model.addAttribute("action", "reception.action.add");
        return "patient";
    }

    @GetMapping("/{id:\\d+}/edit")
    public String editPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.findById(id));
        model.addAttribute("mappingUri", "%d/%s".formatted(id, "edit"));
        model.addAttribute("action", "reception.action.edit");
        return "patient";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String editPatient(@PathVariable Long id,
                              @Valid @ModelAttribute("patient") PatientDto patientDto,
                              BindingResult bindingResult,
                              Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                patientService.update(id, patientDto);
                return "redirect:/patients";
            } catch (PatientAlreadyExistsException e) {
                bindingResult.rejectValue("email", e.getMessage());
            }
        }

        model.addAttribute("mappingUri", "%d/%s".formatted(id, "edit"));
        model.addAttribute("action", "reception.action.edit");
        return "patient";
    }

    @PostMapping("/{id:\\d+}/delete")
    public String deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return "redirect:/patients";
    }
}

