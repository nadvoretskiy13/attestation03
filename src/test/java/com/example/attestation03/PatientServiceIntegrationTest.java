package com.example.attestation03;

import com.example.attestation03.model.Patient;
import com.example.attestation03.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PatientServiceIntegrationTest {

    @Autowired
    private PatientRepository repo;

    @Test
    void createAndFindPatient() {
        Patient p = Patient.builder()
                .firstName("Иван")
                .lastName("Иванов")
                .passportNumber("AB123456")
                .phone("+70000000000")
                .email("ivan@example.com")
                .birthDate(LocalDate.of(1990,1,1))
                .build();
        Patient saved = repo.save(p);
        List<Patient> all = repo.findByDeletedFalse();
        assertThat(all).isNotEmpty();
        assertThat(all.get(0).getId()).isEqualTo(saved.getId());
    }
}
