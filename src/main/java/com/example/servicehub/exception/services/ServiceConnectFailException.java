package com.example.servicehub.exception.services;

public class ServiceConnectFailException extends RuntimeException {
    public ServiceConnectFailException(String message) {
        super(message);
    }
}
