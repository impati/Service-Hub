package com.example.servicehub.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@TestConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TestSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf();

        httpSecurity.anonymous();

        httpSecurity.authorizeRequests(auth -> {
            auth.mvcMatchers("/service/registration").hasRole("ADMIN");
            auth.mvcMatchers("/customer/**").hasRole("USER");
            auth.mvcMatchers("/comments/**").hasRole("USER");
            auth.mvcMatchers("/requested-service/registration/**").hasRole("USER");
            auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
            auth.anyRequest().permitAll();
        });

        return httpSecurity.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

}
