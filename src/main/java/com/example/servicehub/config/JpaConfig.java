package com.example.servicehub.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
public class JpaConfig {

    private final EntityManager em;

    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of("impati"); // TODO : 스프링 시큐리티 인증 기능 붙일 때 수정
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }

}
