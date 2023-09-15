package com.example.servicehub.security.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.client.RestTemplate;

import com.example.servicehub.config.CustomerServerConfig;
import com.example.servicehub.security.authentication.LoginAuthenticationProvider;
import com.example.servicehub.security.filter.AuthorizationRedirectFilter;
import com.example.servicehub.security.filter.CustomerServerSignupFilter;
import com.example.servicehub.security.filter.LoginAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomerServerConfig customerServerConfig;
	private final RestTemplate restTemplate;

	@Bean
	public SecurityFilterChain securityFilterChain(
		final HttpSecurity httpSecurity,
		final AuthenticationManagerBuilder authenticationManagerBuilder
	) throws Exception {

		httpSecurity.csrf();

		httpSecurity.anonymous();

		httpSecurity.authorizeRequests(auth -> {
			auth.mvcMatchers("/service/registration").hasRole("ADMIN");
			auth.mvcMatchers("/customer/**").hasRole("USER");
			auth.mvcMatchers("/comments/**").hasRole("USER");
			auth.mvcMatchers("/requested-service/registration/**").hasRole("USER");
			auth.requestMatchers(toStaticResources().atCommonLocations()).permitAll();
			auth.anyRequest().permitAll();
		});

		httpSecurity.addFilterBefore(loginAuthenticationFilter(authenticationManagerBuilder),
			AnonymousAuthenticationFilter.class);
		httpSecurity.addFilterBefore(new AuthorizationRedirectFilter(customerServerConfig),
			AnonymousAuthenticationFilter.class);
		httpSecurity.addFilterBefore(new CustomerServerSignupFilter(customerServerConfig),
			AnonymousAuthenticationFilter.class);

		httpSecurity.exceptionHandling()
			.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/customer-server/authorization"));
		httpSecurity
			.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.deleteCookies("JSESSIONID", "remember-me")
			.logoutSuccessUrl("/");

		return httpSecurity.build();
	}

	public LoginAuthenticationFilter loginAuthenticationFilter(
		final AuthenticationManagerBuilder authenticationManagerBuilder
	) throws Exception {
		authenticationManagerBuilder.authenticationProvider(
			new LoginAuthenticationProvider(customerServerConfig, restTemplate)
		);
		final AuthenticationManager authenticationManager = authenticationManagerBuilder.getOrBuild();
		final LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(
			customerServerConfig,
			restTemplate
		);
		loginAuthenticationFilter.setAuthenticationManager(authenticationManager);
		return loginAuthenticationFilter;
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
