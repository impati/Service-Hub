package com.example.servicehub.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf();

        httpSecurity.anonymous();

        httpSecurity.authorizeRequests(auth-> {
            auth.mvcMatchers("/service/registration").hasRole("ADMIN");
            auth.mvcMatchers("/client/**").hasRole("USER");
            auth.mvcMatchers("/comments/**").hasRole("USER");
            auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
            auth.anyRequest().permitAll();
        });

        return httpSecurity.build();
    }
}
