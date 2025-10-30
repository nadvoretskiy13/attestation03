package com.example.attestation03.controller;

import com.example.attestation03.model.Patient;
import com.example.attestation03.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class PatientWebController {

    private final PatientService service;

    public PatientWebController(PatientService service) {
        this.service = service;
    }

    // Список пациентов + форма для добавления
    @GetMapping
    public String listPatients(Model model) {
        model.addAttribute("patients", service.listAll());
        model.addAttribute("newPatient", new Patient());
        return "patients";
    }

    // Добавление нового пациента
    @PostMapping
    public String addPatient(@ModelAttribute("newPatient") Patient patient) {
        service.create(patient);
        return "redirect:/patients";
    }

    // Удаление пациента (soft delete)
    @PostMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        service.softDelete(id);
        return "redirect:/patients";
    }

    // Форма редактирования
    @GetMapping("/edit/{id}")
    public String editPatient(@PathVariable Long id, Model model) {
        service.get(id).ifPresent(patient -> model.addAttribute("editPatient", patient));
        return "edit-patient";
    }

    // Сохранение изменений
    @PostMapping("/edit")
    public String updatePatient(@ModelAttribute("editPatient") Patient patient) {
        service.update(patient.getId(), patient);
        return "redirect:/patients";
    }
}