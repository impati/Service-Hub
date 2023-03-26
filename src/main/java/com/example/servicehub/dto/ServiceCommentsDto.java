package com.example.servicehub.dto;

import com.example.servicehub.domain.ServiceComment;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ServiceCommentsDto {

    private Long commentId;
    private String content;
    private Long clientId;
    private String nickname;
    private LocalDate createAt;
    private LocalDate lastUpdateAt;

    private ServiceCommentsDto(Long commentId, String content, Long clientId, String nickname, LocalDate createAt, LocalDate lastUpdateAt) {
        this.commentId = commentId;
        this.content = content;
        this.clientId = clientId;
        this.nickname = nickname;
        this.createAt = createAt;
        this.lastUpdateAt = lastUpdateAt;
    }

    public static ServiceCommentsDto of(ServiceComment serviceComment) {
        return new ServiceCommentsDto(
                serviceComment.getId(),
                serviceComment.getContent(),
                serviceComment.getClientId(),
                serviceComment.getNickname(),
                serviceComment.getCreatedAt(),
                serviceComment.getUpdatedAt());
    }
}
