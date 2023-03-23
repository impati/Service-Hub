package com.example.servicehub.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CustomerServer {

    @Value("${customer.server}")
    private String targetUrl;

    @Value("${customer.clientId}")
    private String clientId;
}
