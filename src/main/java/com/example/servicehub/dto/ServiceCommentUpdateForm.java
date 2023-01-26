package com.example.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCommentUpdateForm {
    private Long commentId;
    private Long clientId;
    private String content;
    private Long serviceId;

    public ServiceCommentUpdateForm(Long commentId, Long clientId, String content) {
        this.commentId = commentId;
        this.clientId = clientId;
        this.content = content;
    }

    public void assignClient(Long clientId){
        this.clientId = clientId;
    }
}
