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
                Patient p1 = new Patient();
                p1.setFirstName("Иван");
                p1.setLastName("Иванов");
                p1.setPassportNumber("AB123456");
                p1.setPhone("+70000000000");
                p1.setEmail("ivan@example.com");
                p1.setBirthDate(LocalDate.of(1990,1,1));
                repo.save(p1);
            }
        };
    }
}
