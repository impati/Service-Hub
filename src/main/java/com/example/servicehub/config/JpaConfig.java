package com.example.servicehub.config;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaConfig {

	private final EntityManager em;

	@Bean
	public AuditorAware<String> auditorAware() {
		return () -> Optional.ofNullable(SecurityContextHolder.getContext())
			.map(SecurityContext::getAuthentication)
			.filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
			.filter(Authentication::isAuthenticated)
			.map(Authentication::getPrincipal)
			.map(CustomerPrincipal.class::cast)
			.map(CustomerPrincipal::getNickname);
	}

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(em);
	}
}
