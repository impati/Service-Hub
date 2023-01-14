package com.example.servicehub.domain.constant;

import lombok.Getter;

/**
 *  TODO:엔티티로 승격 OR 계층구조
 */
@Getter
public enum CustomRole {
    USER("USER"),ADMIN("ADMIN");

    private final String value;

    CustomRole(String value) {
        this.value = value;
    }
}
