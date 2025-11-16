package com.example.registry.service;

import com.example.registry.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> findActive();
    Optional<Patient> get(Long id);
    Patient save(Patient patient);
    Optional<Patient> update(Long id, Patient patient);
    boolean softDelete(Long id);
}
