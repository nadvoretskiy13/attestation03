package com.example.attestation03.service;

import com.example.attestation03.model.Appointment;
import com.example.attestation03.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepository repo;
    public AppointmentService(AppointmentRepository repo) { this.repo = repo; }

    public List<Appointment> listAll() { return repo.findByDeletedFalse(); }
    public Optional<Appointment> get(Long id) { return repo.findById(id).filter(a -> !a.isDeleted()); }
    public Appointment create(Appointment a) { a.setId(null); a.setDeleted(false); return repo.save(a); }
    public Optional<Appointment> update(Long id, Appointment updated) {
        return repo.findById(id).filter(a -> !a.isDeleted()).map(ex -> {
            ex.setAppointmentAt(updated.getAppointmentAt());
            ex.setDoctor(updated.getDoctor());
            ex.setPatient(updated.getPatient());
            ex.setNotes(updated.getNotes());
            return repo.save(ex);
        });
    }
    public boolean softDelete(Long id) {
        return repo.findById(id).filter(a -> !a.isDeleted()).map(a -> {
            a.setDeleted(true);
            repo.save(a);
            return true;
        }).orElse(false);
    }
}
