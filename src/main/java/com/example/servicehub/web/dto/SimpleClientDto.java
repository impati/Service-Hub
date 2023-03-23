package com.example.servicehub.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleClientDto {

    private String nickname;
    private Long clientId;
    private String blogUrl;
    private String profileUrl;
    private String introComment;

    @Builder
    public SimpleClientDto(String nickname, Long clientId, String blogUrl, String profileUrl, String introComment) {
        this.nickname = nickname;
        this.clientId = clientId;
        this.blogUrl = blogUrl;
        this.profileUrl = profileUrl;
        this.introComment = introComment;
    }
}
