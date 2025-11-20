package com.example.reception.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReceptionUserDto implements Serializable {
    @NotBlank(message = "{reception.form.field.empty}")
    String username;

    @NotBlank(message = "{reception.form.field.empty}")
    String password;

    @NotBlank(message = "{reception.form.field.empty}")
    String passwordConfirm;
}