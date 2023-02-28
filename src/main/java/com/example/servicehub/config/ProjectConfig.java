package com.example.servicehub.config;

import com.example.servicehub.support.JsoupMetaDataCrawler;
import com.example.servicehub.support.LogoManager;
import com.example.servicehub.support.MetaDataCrawler;
import com.example.servicehub.support.ProfileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProjectConfig {

    @Bean
    public MetaDataCrawler metaDataCrawler(){ return new JsoupMetaDataCrawler(); }

    @Bean(name= "logo")
    public LogoManager logoManager(){
        return new LogoManager();
    }

    @Bean(name= "profile")
    public ProfileManager profileManager(){
        return new ProfileManager();
    }

}
