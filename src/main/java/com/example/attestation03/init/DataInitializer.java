package com.example.attestation03.init;

import com.example.attestation03.model.*;
import com.example.attestation03.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(PatientRepository patientRepo,
                           DoctorRepository doctorRepo,
                           AppointmentRepository appointmentRepo) {
        return args -> {
            if (doctorRepo.count() == 0) {
                Doctor d1 = doctorRepo.save(Doctor.builder().firstName("Алексей").lastName("Петров").specialization("Терапевт").phone("+7-900-000-01").email("alex@clinic.local").build());
                Doctor d2 = doctorRepo.save(Doctor.builder().firstName("Мария").lastName("Сидорова").specialization("Хирург").phone("+7-900-000-02").email("maria@clinic.local").build());
            }

            if (patientRepo.findByDeletedFalse().isEmpty()) {
                Patient p = Patient.builder().firstName("Иван").lastName("Иванов").passportNumber("AB123456").phone("+70000000000").email("ivan@example.com").birthDate(LocalDate.of(1990,1,1)).deleted(false).build();
                Patient saved = patientRepo.save(p);

                appointmentRepo.save(Appointment.builder().patient(saved).doctor(doctorRepo.findAll().get(0)).appointmentAt(LocalDateTime.now().plusDays(1)).notes("Первичный приём").deleted(false).build());
            }
        };
    }
}
