package com.example.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ServiceCommentUpdateForm {
    private Long serviceCommentsId;
    private Long clientId;
    private String content;
    private Long serviceId;

    public ServiceCommentUpdateForm(Long serviceCommentsId, Long clientId, String content) {
        this.serviceCommentsId = serviceCommentsId;
        this.clientId = clientId;
        this.content = content;
    }
}
