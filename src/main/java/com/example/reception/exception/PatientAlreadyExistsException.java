package com.example.reception.exception;

public class PatientAlreadyExistsException extends RuntimeException {

    public PatientAlreadyExistsException() {
        super("reception.patient.email.exists");
    }
}
