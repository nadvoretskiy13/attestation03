package com.example.reception.controller;

import com.example.reception.exception.PatientAlreadyExistsException;
import com.example.reception.exception.PatientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionRestControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("errors", createErrorList("error", ex)));
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<?> handlePatientNotFoundException(PatientNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("errors", createErrorList("id", ex)));
    }

    @ExceptionHandler(PatientAlreadyExistsException.class)
    public ResponseEntity<?> handlePatientAlreadyExistsException(PatientAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("errors", createErrorList("email", ex)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errors", ex.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> new ErrorDetail(fieldError.getField(), fieldError.getDefaultMessage()))
                        .toList()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errors", createErrorList("error", ex)));
    }

    private List<ErrorDetail> createErrorList(String field, Exception ex) {
        String message = messageSource.getMessage(ex.getMessage(), null, ex.getMessage(), null);
        return List.of(new ErrorDetail(field, message));
    }

    record ErrorDetail(String field, String message) implements Serializable {
    }
}
