package com.example.servicehub.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestStatus {
    BEFORE("요청 대기"),
    FAIL("요청 거절"),
    DEFER("요청 보류"),
    COMPLETE("요청 완료");

    private final String name;
}
