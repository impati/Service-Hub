package com.example.servicehub.config;

import com.example.servicehub.support.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(CustomerServer.class)
@Configuration
@RequiredArgsConstructor
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
}
