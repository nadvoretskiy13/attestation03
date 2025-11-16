package com.example.registry.controller;

import com.example.registry.model.Patient;
import com.example.registry.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Patient API")
public class PatientRestController {

    private final PatientService service;

    public PatientRestController(PatientService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List patients (not deleted)")
    public List<Patient> all() {
        return service.findActive();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> get(@PathVariable Long id) {
        return service.get(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody Patient p) {
        Patient saved = service.save(p);
        return ResponseEntity.created(URI.create("/api/patients/" + saved.getId()))
                .body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean ok = service.softDelete(id);
        return ok ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}
