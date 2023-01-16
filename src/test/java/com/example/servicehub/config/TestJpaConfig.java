package com.example.servicehub.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@EnableJpaAuditing
@TestConfiguration
public class TestJpaConfig {
    @PersistenceContext  private EntityManager em;

    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of("impati"); // TODO : 스프링 시큐리티 인증 기능 붙일 때 수정
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }
}
