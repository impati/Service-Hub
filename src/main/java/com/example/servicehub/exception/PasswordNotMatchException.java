package com.example.servicehub.exception;

public class PasswordNotMatchException extends RuntimeException {

    public PasswordNotMatchException() {
    }

    public PasswordNotMatchException(String message) {
        super(message);
    }
}
