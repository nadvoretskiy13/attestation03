package com.example.attestation03.service;

import com.example.attestation03.model.Patient;
import com.example.attestation03.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository repo;

    public PatientService(PatientRepository repo) {
        this.repo = repo;
    }

    public List<Patient> listAll() {
        return repo.findByDeletedFalse();
    }

    public Optional<Patient> get(Long id) {
        return repo.findByIdAndDeletedFalse(id);
    }

    public Patient create(Patient patient) {
        patient.setId(null);
        patient.setDeleted(false);
        return repo.save(patient);
    }

    public Optional<Patient> update(Long id, Patient updated) {
        return repo.findByIdAndDeletedFalse(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setPassportNumber(updated.getPassportNumber());
            existing.setPhone(updated.getPhone());
            existing.setEmail(updated.getEmail());
            existing.setBirthDate(updated.getBirthDate());
            return repo.save(existing);
        });
    }

    public boolean softDelete(Long id) {
        return repo.findByIdAndDeletedFalse(id).map(p -> {
            p.setDeleted(true);
            repo.save(p);
            return true;
        }).orElse(false);
    }
}
