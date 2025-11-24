package com.example.reception.controller;

import com.example.reception.exception.PatientNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionWebControllerAdvice {

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException(Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", "reception.page.not_found");
        return "error/404";
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public String handleNoPatientFoundException(Model model, HttpServletResponse response, PatientNotFoundException e) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", e.getMessage());
        return "error/404";
    }
}
