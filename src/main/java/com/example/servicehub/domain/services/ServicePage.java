package com.example.servicehub.domain.services;

import lombok.Getter;

@Getter
public enum ServicePage {

    POPULARITY("popularity"),
    CLICK("click");
    private final String name;

    public static final int DEFAULT_SIZE = 10;
    public static final int DEFAULT_START_PAGE = 0;

    ServicePage(String name) {
        this.name = name;
    }

}
