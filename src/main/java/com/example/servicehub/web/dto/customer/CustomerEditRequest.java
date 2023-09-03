package com.example.servicehub.web.dto.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerEditRequest {

    private String nickname;
    private String introduceComment;
    private String blogUrl;
    private String profileUrl;

    @Builder
    public CustomerEditRequest(
        final String nickname,
        final String introduceComment,
        final String blogUrl,
        final String profileUrl
    ) {
        this.nickname = nickname;
        this.introduceComment = introduceComment;
        this.blogUrl = blogUrl;
        this.profileUrl = profileUrl;
    }
}
