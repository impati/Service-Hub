package com.example.servicehub.config;

import com.example.servicehub.support.DefaultImageResizer;
import com.example.servicehub.support.ImageResizer;
import com.example.servicehub.support.LogoManager;
import com.example.servicehub.support.ProfileManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ProjectTestConfig {

    @Bean(name = "logo")
    public LogoManager logoManager(){
        return new LogoManager(imageResizer());
    }

    @Bean(name = "profile")
    public ProfileManager profileManager(){
        return new ProfileManager();
    }

    @Bean
    public ImageResizer imageResizer(){ return new DefaultImageResizer();}

}
