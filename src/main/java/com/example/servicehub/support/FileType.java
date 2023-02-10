package com.example.servicehub.support;

import lombok.Getter;

@Getter
public enum FileType {

    logo("logo"),profile("profile");

    private final String type;

    FileType(String type) {
        this.type = type;
    }
}

