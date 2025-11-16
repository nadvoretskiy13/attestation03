package com.example.registry.init;

import com.example.registry.model.Patient;
import com.example.registry.repository.PatientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(PatientRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                Patient p1 = Patient.builder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .passportNumber("AB123456")
                        .phone("+70000000000")
                        .email("ivan@example.com")
                        .birthDate(LocalDate.of(1990,1,1))
                        .deleted(false)
                        .build();
                repo.save(p1);
            }
        };
    }
}
