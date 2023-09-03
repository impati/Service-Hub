package com.example.servicehub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("customer")
public class CustomerServer {

	private String clientId;
	private String server;
}
