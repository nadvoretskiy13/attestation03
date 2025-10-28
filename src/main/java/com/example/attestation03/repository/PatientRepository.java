package com.example.attestation03.repository;

import com.example.attestation03.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByDeletedFalse();
    Optional<Patient> findByIdAndDeletedFalse(Long id);
}
