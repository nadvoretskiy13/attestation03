package com.example.reception.exception;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException() {
        super("reception.patient.not_found");
    }
}
