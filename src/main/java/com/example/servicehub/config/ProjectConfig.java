package com.example.servicehub.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.example.servicehub.support.crawl.JsoupMetaDataCrawler;
import com.example.servicehub.support.crawl.MetaDataCrawler;
import com.example.servicehub.support.file.DefaultImageResizer;
import com.example.servicehub.support.file.ImageResizer;
import com.example.servicehub.support.file.LogoManager;
import com.example.servicehub.support.file.ProfileManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({CustomerServerConfig.class, ServerConfig.class})
public class ProjectConfig {

	@Bean
	public MetaDataCrawler metaDataCrawler() {
		return new JsoupMetaDataCrawler();
	}

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

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	}
}
