package com.example.servicehub.security.config;

import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.security.SignupManager;
import com.example.servicehub.security.authentication.CustomOAuth2UserService;
import com.example.servicehub.security.filter.KeycloakAuthenticationFilter;
import com.example.servicehub.security.filter.PasswordGrantTypeLoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final KeycloakAuthenticationFilter keycloakAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final DefaultOAuth2AuthorizedClientManager clientManager;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf();

        httpSecurity.anonymous();

        httpSecurity.authorizeRequests(auth-> {
                    auth.mvcMatchers("/service/registration").hasRole("ADMIN");
                    auth.mvcMatchers("/client/**").hasRole("USER");
                    auth.mvcMatchers("/comments/**").hasRole("USER");
                    auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
                    auth.anyRequest().permitAll();
        });


        httpSecurity
                .oauth2Client()
                .and()
                .addFilterAfter(new PasswordGrantTypeLoginFilter(clientManager,customOAuth2UserService),AnonymousAuthenticationFilter.class);

        httpSecurity.addFilterBefore(keycloakAuthenticationFilter, OAuth2LoginAuthenticationFilter.class);


        httpSecurity
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        httpSecurity
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID", "remember-me");

        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        return httpSecurity.build();
    }



}
