package com.example.reception.repository;

import com.example.reception.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByEmail(String email);
}