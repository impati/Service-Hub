package com.example.servicehub.domain;

import lombok.Getter;

@Getter
public enum ServiceSortType {

    POPULARITY("popularity");

    private final String name;

    ServiceSortType(String name) {
        this.name = name;
    }

}
