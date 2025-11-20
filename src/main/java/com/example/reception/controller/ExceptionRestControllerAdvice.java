package com.example.reception.controller;

import com.example.reception.exception.PatientAlreadyExistsException;
import com.example.reception.exception.PatientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionRestControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<?> handlePatientNotFoundException(PatientNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(messageSource.getMessage(ex.getMessage(), null, null));
    }

    @ExceptionHandler(PatientAlreadyExistsException.class)
    public ResponseEntity<?> handlePatientAlreadyExistsException(PatientAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(messageSource.getMessage(ex.getMessage(), null, null));
    }
}
