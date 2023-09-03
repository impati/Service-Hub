package com.example.servicehub.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.servicehub.support.file.DefaultImageResizer;
import com.example.servicehub.support.file.ImageResizer;
import com.example.servicehub.support.file.LogoManager;
import com.example.servicehub.support.file.ProfileManager;

@TestConfiguration
public class ProjectTestConfig {

	@Bean(name = "logo")
	public LogoManager logoManager() {
		return new LogoManager(imageResizer());
	}

	@Bean(name = "profile")
	public ProfileManager profileManager() {
		return new ProfileManager();
	}

	@Bean
	public ImageResizer imageResizer() {
		return new DefaultImageResizer();
	}
}
