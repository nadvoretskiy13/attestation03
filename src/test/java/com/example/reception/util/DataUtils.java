package com.example.reception.util;

import com.example.reception.dto.PatientDto;
import com.example.reception.entity.Patient;

import java.time.LocalDate;

public class DataUtils {

    public static Patient getJohnDoeTransient() {
        return Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .phone("123456789")
                .email("johndoe@example.com")
                .passport("12345")
                .birthDate(LocalDate.of(1980, 1, 1))
                .build();
    }

    public static Patient getAdamSmithTransient() {
        return Patient.builder()
                .firstName("Adam")
                .lastName("Smith")
                .phone("234567890")
                .email("adamsmith@example.com")
                .passport("23456")
                .birthDate(LocalDate.of(1985, 5, 5))
                .build();
    }

    public static Patient getMikeBomTransient() {
        return Patient.builder()
                .firstName("Mike")
                .lastName("Bom")
                .phone("123123123")
                .email("mikebom@example.com")
                .passport("567567")
                .birthDate(LocalDate.of(1990, 12, 12))
                .isDeleted(true)
                .build();
    }

    public static Patient getJohnDoePersisted() {
        return Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .phone("123456789")
                .email("johndoe@example.com")
                .passport("12345")
                .birthDate(LocalDate.of(1980, 1, 1))
                .build();
    }

    public static Patient getAdamSmithPersisted() {
        return Patient.builder()
                .id(2L)
                .firstName("Adam")
                .lastName("Smith")
                .phone("234567890")
                .email("adamsmith@example.com")
                .passport("23456")
                .birthDate(LocalDate.of(1985, 5, 5))
                .build();
    }

    public static Patient getMikeBomPersisted() {
        return Patient.builder()
                .id(3L)
                .firstName("Mike")
                .lastName("Bom")
                .phone("123123123")
                .email("mikebom@example.com")
                .passport("567567")
                .birthDate(LocalDate.of(1990, 12, 12))
                .isDeleted(true)
                .build();
    }

    public static PatientDto getJohnDoeDto() {
        return PatientDto.builder()
                .firstName("John")
                .lastName("Doe")
                .phone("123456789")
                .email("johndoe@example.com")
                .passport("12345")
                .birthDate(LocalDate.of(1980, 1, 1))
                .build();
    }

    public static PatientDto getAdamSmithDto() {
        return PatientDto.builder()
                .firstName("Adam")
                .lastName("Smith")
                .phone("234567890")
                .email("adamsmith@example.com")
                .passport("23456")
                .birthDate(LocalDate.of(1985, 5, 5))
                .build();
    }

    public static PatientDto getMikeBomDto() {
        return PatientDto.builder()
                .firstName("Mike")
                .lastName("Bom")
                .phone("123123123")
                .email("mikebom@example.com")
                .passport("567567")
                .birthDate(LocalDate.of(1990, 12, 12))
                .build();
    }

    public static PatientDto getInvalidDto() {
        return PatientDto.builder()
                .firstName("Mike")
                .lastName("Bom")
                .email("mikebom")
                .phone("567567")
                .birthDate(LocalDate.of(1990, 12, 12))
                .build();
    }
}
