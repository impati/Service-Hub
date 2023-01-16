package com.example.servicehub.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PopularityService {
    private Long popularity;
    private String serviceName;
    private String logoStoreName;
    private String serviceUrl;
    private String title;

    public PopularityService(Long popularity, String serviceName, String logoStoreName, String serviceUrl, String title) {
        this.popularity = Long.valueOf(popularity);
        this.serviceName = serviceName;
        this.logoStoreName = logoStoreName;
        this.serviceUrl = serviceUrl;
        this.title = title;
    }
}
