package com.example.servicehub.security.Exception;

public class UserDetailsTypeMisMatchException extends RuntimeException{
    public UserDetailsTypeMisMatchException() {
    }

    public UserDetailsTypeMisMatchException(String message) {
        super(message);
    }
}
