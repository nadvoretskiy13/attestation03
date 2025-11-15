package com.example.attestation03.controller;

import com.example.attestation03.model.Doctor;
import com.example.attestation03.service.DoctorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@Tag(name = "Врачи", description = "API врачей")
public class ApiDoctorController {

    private final DoctorService service;

    public ApiDoctorController(DoctorService service) { this.service = service; }

    @GetMapping
    public List<Doctor> all() { return service.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> get(@PathVariable Long id) {
        return service.get(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Doctor> create(@RequestBody Doctor d) {
        Doctor saved = service.create(d);
        return ResponseEntity.created(URI.create("/api/doctors/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> update(@PathVariable Long id, @RequestBody Doctor d) {
        return service.update(id, d).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
