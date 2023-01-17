package com.example.servicehub.dto;

import com.example.servicehub.domain.ServiceComment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ServiceCommentsDto {
    private String content;
    private Long clientId;
    private LocalDate createAt;
    private LocalDate lastUpdateAt;

    public static ServiceCommentsDto of(ServiceComment serviceComment){
        return new ServiceCommentsDto(
                serviceComment.getContent(),
                serviceComment.getClient().getId(),
                serviceComment.getCreatedAt(),
                serviceComment.getUpdatedAt());
    }
}
