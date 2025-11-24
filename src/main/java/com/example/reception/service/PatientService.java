package com.example.reception.service;

import com.example.reception.dto.PatientDto;
import com.example.reception.entity.Patient;

import java.util.List;

public interface PatientService {

    List<Patient> findAll();

    Patient create(PatientDto patient);

    Patient update(Long id, PatientDto patientDto);

    void delete(Long id);

    Patient findById(Long id);
}
