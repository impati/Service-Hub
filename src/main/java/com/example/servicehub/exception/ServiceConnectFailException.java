package com.example.servicehub.exception;

public class ServiceConnectFailException extends RuntimeException{
    public ServiceConnectFailException(String message) {
        super(message);
    }
}
