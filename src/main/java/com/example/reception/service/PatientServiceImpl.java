package com.example.reception.service;

import com.example.reception.dto.PatientDto;
import com.example.reception.entity.Patient;
import com.example.reception.exception.PatientAlreadyExistsException;
import com.example.reception.exception.PatientNotFoundException;
import com.example.reception.mapper.PatientMapper;
import com.example.reception.repository.PatientRepository;
import com.example.reception.repository.filter.SoftDeletedFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final SoftDeletedFilter softDeletedFilter;

    @Override
    @Transactional(readOnly = true)
    public Patient findById(Long id) {
        softDeletedFilter.filterName("deletedPatientFilter").filterParameter("isDeleted");
        return softDeletedFilter.apply(() -> patientRepository.findById(id), false)
                .orElseThrow(PatientNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        softDeletedFilter.filterName("deletedPatientFilter").filterParameter("isDeleted");
        return softDeletedFilter.apply(patientRepository::findAll, false);
    }

    @Override
    @Transactional
    public Patient create(PatientDto patientDto) {
        validateEmailNotExists(patientDto.email());
        Patient patient = patientMapper.toEntity(patientDto);
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public Patient update(Long id, PatientDto patientDto) {
        var patient = findById(id);
        if (!Objects.equals(patientDto.email(), patient.getEmail())) {
            validateEmailNotExists(patientDto.email());
        }
        return patientMapper.updateFromDto(patientDto, patient);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        patientRepository.deleteById(id);
    }

    private void validateEmailNotExists(String email) {
        if (patientRepository.existsByEmail(email)) {
            throw new PatientAlreadyExistsException();
        }
    }
}
