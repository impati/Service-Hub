package com.example.servicehub.web.validator;

import lombok.Getter;

@Getter
public enum UniqueType {
    USERNAME("username"),EMAIL("email");

    private final String name;

    UniqueType(String name) {
        this.name = name;
    }
}
