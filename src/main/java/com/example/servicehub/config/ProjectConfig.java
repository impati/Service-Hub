package com.example.servicehub.config;

import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.support.LogoManager;
import com.example.servicehub.support.MetaDataCrawler;
import com.example.servicehub.support.ProfileManager;
import com.example.servicehub.web.validator.KeycloakUsernameUniqueValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProjectConfig {

    private final ClientRepository clientRepository;

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

    @Bean
    public KeycloakUsernameUniqueValidator keycloakUsernameUniqueValidator(){
        return new KeycloakUsernameUniqueValidator(clientRepository);
    }

}
