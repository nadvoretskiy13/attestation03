package com.example.reception.controller;

import com.example.reception.dto.ReceptionUserDto;
import com.example.reception.exception.UserAlreadyExistsException;
import com.example.reception.service.ReceptionUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class ReceptionUserController {

    private final ReceptionUserService receptionUserService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new ReceptionUserDto());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@Valid @ModelAttribute("user") ReceptionUserDto user,
                             BindingResult bindingResult) {
        boolean hasErrors = bindingResult.hasErrors();

        if (!hasErrors && !Objects.equals(user.getPassword(), user.getPasswordConfirm())) {
                bindingResult.rejectValue("passwordConfirm", "reception.user.password.mismatch");
                hasErrors = true;
        }

        if (!hasErrors) {
            try {
                receptionUserService.register(user);
            } catch (UserAlreadyExistsException e) {
                bindingResult.rejectValue("username", e.getMessage());
                hasErrors = true;
            }
        }

        if (hasErrors) {
            return "register";
        }
        return "redirect:/";
    }

}

