package com.example.servicehub.exception;

public class FileResizeFailException extends RuntimeException{

    public FileResizeFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
