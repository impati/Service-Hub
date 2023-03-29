package com.example.servicehub.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleCustomerDto {

    private String nickname;
    private Long customerId;
    private String blogUrl;
    private String profileUrl;
    private String introComment;

    @Builder
    public SimpleCustomerDto(String nickname, Long customerId, String blogUrl, String profileUrl, String introComment) {
        this.nickname = nickname;
        this.customerId = customerId;
        this.blogUrl = blogUrl;
        this.profileUrl = profileUrl;
        this.introComment = introComment;
    }
}
