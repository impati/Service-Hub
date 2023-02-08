package com.example.servicehub.config;

import com.example.servicehub.support.LogoManager;
import com.example.servicehub.support.MetaDataCrawler;
import com.example.servicehub.support.ProfileManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public MetaDataCrawler metaDataCrawler(){
        return new MetaDataCrawler();
    }

    @Bean
    public LogoManager logoManager(){
        return new LogoManager();
    }

    @Bean
    public ProfileManager profileManager(){
        return new ProfileManager();
    }


}
