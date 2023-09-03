package com.example.servicehub.web;

import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.example.servicehub.domain.customer.RoleType;
import com.example.servicehub.security.authentication.CustomerPrincipal;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

	private final static String prefix = "ROLE_";

	@Override
	public SecurityContext createSecurityContext(final WithMockCustomUser customUser) {
		final String role = prefix + customUser.role();
		final CustomerPrincipal customerPrincipal = CustomerPrincipal.builder()
			.id(customUser.id())
			.username(customUser.username())
			.nickname(customUser.username())
			.profileImageUrl("test")
			.email("test")
			.blogUrl("test")
			.introduceComment("test")
			.roleType(RoleType.of(role))
			.authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
			.build();

		final Authentication authentication = UsernamePasswordAuthenticationToken
			.authenticated(customerPrincipal, getAccessToken(), customerPrincipal.getAuthorities());
		final SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
	}

	private String getAccessToken() {
		return "Bearer accessToken";
	}
}
