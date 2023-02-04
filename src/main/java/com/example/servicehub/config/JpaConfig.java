package com.example.servicehub.config;

import com.example.servicehub.security.authentication.ClientPrincipal;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.validation.constraints.Size;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
public class JpaConfig {

    private final EntityManager em;

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                        .map(SecurityContext::getAuthentication)
                        .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
                        .filter(Authentication::isAuthenticated)
                        .map(Authentication::getPrincipal)
                        .map(ClientPrincipal.class::cast)
                        .map(ClientPrincipal::getEmail);
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }

}
