package com.example.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ServiceCommentForm {
    private Long serviceId;
    private Long clientId;
    private String content;


    public void assignAnAuthor(Long clientId){
        this.clientId = clientId;
    }
}
