package com.example.servicehub.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PopularityServiceDto {
    private Long popularity;
    private String serviceName;
    private String logoStoreName;
    private String serviceUrl;
    private String title;
    private Long serviceId;
    private boolean isPossess;

    public PopularityServiceDto(Long popularity, String serviceName, String logoStoreName, String serviceUrl, String title, Long serviceId) {
        this.popularity = popularity;
        this.serviceName = serviceName;
        this.logoStoreName = logoStoreName;
        this.serviceUrl = serviceUrl;
        this.title = title;
        this.serviceId = serviceId;
        this.isPossess = false;
    }

}
