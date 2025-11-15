package com.example.attestation03.controller;

import com.example.attestation03.model.Appointment;
import com.example.attestation03.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Приёмы", description = "API приёмов/записей")
public class ApiAppointmentController {
    private final AppointmentService service;
    public ApiAppointmentController(AppointmentService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Список приёмов (не удалённых)")
    public List<Appointment> all() { return service.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> get(@PathVariable Long id) {
        return service.get(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody Appointment a) {
        Appointment saved = service.create(a);
        return ResponseEntity.created(URI.create("/api/appointments/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(@PathVariable Long id, @RequestBody Appointment a) {
        return service.update(id, a).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean ok = service.softDelete(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
