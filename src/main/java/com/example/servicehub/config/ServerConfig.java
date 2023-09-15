package com.example.servicehub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("project")
public class ServerConfig {

	private String domain;
}
