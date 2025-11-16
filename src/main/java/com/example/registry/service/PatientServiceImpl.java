package com.example.registry.service;

import com.example.registry.model.Patient;
import com.example.registry.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repo;

    public PatientServiceImpl(PatientRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Patient> findActive() {
        return repo.findByDeletedFalse();
    }

    @Override
    public Optional<Patient> get(Long id) {
        return repo.findByIdAndDeletedFalse(id);
    }

    @Override
    public Patient save(Patient patient) {
        patient.setId(null);
        patient.setDeleted(false);
        return repo.save(patient);
    }

    @Override
    public Optional<Patient> update(Long id, Patient patient) {
        return repo.findByIdAndDeletedFalse(id).map(ex -> {
            ex.setFirstName(patient.getFirstName());
            ex.setLastName(patient.getLastName());
            ex.setPassportNumber(patient.getPassportNumber());
            ex.setPhone(patient.getPhone());
            ex.setEmail(patient.getEmail());
            ex.setBirthDate(patient.getBirthDate());
            return repo.save(ex);
        });
    }

    @Override
    public boolean softDelete(Long id) {
        return repo.findByIdAndDeletedFalse(id).map(p -> {
            p.setDeleted(true);
            repo.save(p);
            return true;
        }).orElse(false);
    }
}
