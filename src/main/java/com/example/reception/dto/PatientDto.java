package com.example.reception.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record PatientDto(

        @NotBlank(message = "{reception.form.field.empty}")
        String firstName,

        @NotBlank(message = "{reception.form.field.empty}")
        String lastName,

        @NotNull
        @Past
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthDate,

        String phone,

        @Email(message = "{reception.form.field.email}", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
        @NotBlank(message = "{reception.form.field.empty}")
        String email,

        @NotBlank(message = "{reception.form.field.empty}")
        String passport) implements Serializable {
}