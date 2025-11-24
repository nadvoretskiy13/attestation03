package com.example.reception.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("reception.user.username.exists");
    }
}
