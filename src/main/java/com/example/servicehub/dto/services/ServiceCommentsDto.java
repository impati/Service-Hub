package com.example.servicehub.dto.services;

import com.example.servicehub.domain.services.ServiceComment;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ServiceCommentsDto {

    private Long commentId;
    private String content;
    private Long customerId;
    private String nickname;
    private LocalDate createAt;
    private LocalDate lastUpdateAt;

    private ServiceCommentsDto(Long commentId, String content, Long customerId, String nickname, LocalDate createAt, LocalDate lastUpdateAt) {
        this.commentId = commentId;
        this.content = content;
        this.customerId = customerId;
        this.nickname = nickname;
        this.createAt = createAt;
        this.lastUpdateAt = lastUpdateAt;
    }

    public static ServiceCommentsDto of(ServiceComment serviceComment) {
        return new ServiceCommentsDto(
                serviceComment.getId(),
                serviceComment.getContent(),
                serviceComment.getCustomerId(),
                serviceComment.getNickname(),
                serviceComment.getCreatedAt(),
                serviceComment.getUpdatedAt());
    }
}
