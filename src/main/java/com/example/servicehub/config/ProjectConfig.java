package com.example.servicehub.config;

import com.example.servicehub.support.MetaDataCrawler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public MetaDataCrawler metaDataCrawler(){
        return new MetaDataCrawler();
    }
}
