package com.example.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class PopularityServiceDto {
    private Long popularity;
    private String serviceName;
    private String logoStoreName;
    private String serviceUrl;
    private String title;
    private Long serviceId;

}
