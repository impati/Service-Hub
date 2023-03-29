package com.example.servicehub.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("customer")
@Data
public class CustomerServer {
    private String clientId;
    private String server;
}
