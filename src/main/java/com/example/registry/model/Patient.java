package com.example.registry.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String passportNumber;
    private String phone;
    private LocalDate birthDate;
    private String email;

    private boolean deleted = false;

    public Patient() {}

    // getters & setters
    // ...
}
