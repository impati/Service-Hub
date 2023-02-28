package com.example.servicehub.security.config;

import com.example.servicehub.security.authentication.CustomOAuth2UserService;
import com.example.servicehub.security.filter.PasswordGrantTypeLoginFilter;
import com.example.servicehub.security.handler.PasswordGrantTypeLoginFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

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

        PasswordGrantTypeLoginFilter passwordGrantTypeLoginFilter = new PasswordGrantTypeLoginFilter(clientManager, customOAuth2UserService);
        passwordGrantTypeLoginFilter.setAuthenticationFailureHandler(new PasswordGrantTypeLoginFailureHandler());

        httpSecurity
                .oauth2Client()
                .and()
                .addFilterAfter(passwordGrantTypeLoginFilter,AnonymousAuthenticationFilter.class);

        httpSecurity
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        httpSecurity
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login");

        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        return httpSecurity.build();
    }



}
