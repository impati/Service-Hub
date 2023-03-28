package com.example.servicehub.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CustomerEditRequest {
    private String nickname;
    private String introduceComment;
    private String blogUrl;
    private String profileUrl;

    @Builder
    public CustomerEditRequest(String nickname, String introduceComment, String blogUrl, String profileUrl) {
        this.nickname = nickname;
        this.introduceComment = introduceComment;
        this.blogUrl = blogUrl;
        this.profileUrl = profileUrl;
    }
}
