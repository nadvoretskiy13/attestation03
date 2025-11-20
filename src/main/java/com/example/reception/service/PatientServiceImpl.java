package com.example.reception.service;

import com.example.reception.dto.PatientDto;
import com.example.reception.entity.Patient;
import com.example.reception.exception.PatientNotFoundException;
import com.example.reception.exception.PatientAlreadyExistsException;
import com.example.reception.mapper.PatientMapper;
import com.example.reception.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedPatientFilter")
                .setParameter("isDeleted", false);
        var patients =  patientRepository.findAll();
        session.disableFilter("deletedPatientFilter");
        return patients;
    }

    @Override
    @Transactional
    public Patient create(PatientDto patientDto) {
        validateEmailNotExists(patientDto.email());
        return patientRepository.save(patientMapper.toEntity(patientDto));
    }

    @Override
    @Transactional
    public Patient update(Long id, PatientDto patientDto) {
        var patient = patientRepository.findById(id).orElseThrow(PatientNotFoundException::new);
        if (!Objects.equals(patientDto.email(), patient.getEmail())) {
            validateEmailNotExists(patientDto.email());
        }
        var updated = patientMapper.updateFromDto(patientDto, patient);
        return patientRepository.save(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        patientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Patient findById(Long id) {
        return patientRepository.findById(id).orElseThrow(PatientNotFoundException::new);
    }

    private void validateEmailNotExists(String email) {
        if (patientRepository.existsByEmail(email)) {
            throw new PatientAlreadyExistsException("reception.patient.email.exists");
        }
    }
}
