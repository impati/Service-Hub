package com.example.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClickServiceDto {
    private Long click;
    private String serviceName;
    private String logoStoreName;
    private String serviceUrl;
    private String title;
}
