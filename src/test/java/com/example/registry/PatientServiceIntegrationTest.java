package com.example.registry;

import com.example.registry.model.Patient;
import com.example.registry.repository.PatientRepository;
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
                .deleted(false)
                .build();
        Patient saved = repo.save(p);
        List<Patient> all = repo.findByDeletedFalse();
        assertThat(all).isNotEmpty();
        assertThat(all).anyMatch(x -> x.getId().equals(saved.getId()));
    }
}
