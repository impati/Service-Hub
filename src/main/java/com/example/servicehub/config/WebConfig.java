package com.example.servicehub.config;

import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.web.validator.KeycloakUsernameUniqueValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final ClientRepository clientRepository;

    @Bean
    public KeycloakUsernameUniqueValidator keycloakUsernameUniqueValidator(){
        return new KeycloakUsernameUniqueValidator(clientRepository);
    }
}
