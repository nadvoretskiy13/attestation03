package com.example.attestation03.controller;

import com.example.attestation03.model.Patient;
import com.example.attestation03.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Пациенты", description = "API для управления пациентами")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Получить всех пациентов")
    public List<Patient> all() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пациента по ID")
    public ResponseEntity<Patient> get(@PathVariable Long id) {
        return service.get(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать нового пациента")
    public ResponseEntity<Patient> create(@RequestBody Patient patient) {
        Patient saved = service.create(patient);
        return ResponseEntity.created(URI.create("/api/patients/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные пациента")
    public ResponseEntity<Patient> update(@PathVariable Long id, @RequestBody Patient patient) {
        return service.update(id, patient)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft Delete — пометить пациента удалённым")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean ok = service.softDelete(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
