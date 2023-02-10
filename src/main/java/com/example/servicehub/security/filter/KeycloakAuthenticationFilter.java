package com.example.servicehub.security.filter;

import com.example.servicehub.security.SignupManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RequestMatcherRedirectFilter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KeycloakAuthenticationFilter extends OncePerRequestFilter {

    public static final String DEFAULT_FILTER_PROCESSES_URI = "/keycloak/signup/**";

    private final AntPathRequestMatcher antPathRequestMatcher;
    private final SignupManager signupManager;

    public KeycloakAuthenticationFilter(SignupManager signupManager) {
        this.signupManager = signupManager;
        this.antPathRequestMatcher = new AntPathRequestMatcher(DEFAULT_FILTER_PROCESSES_URI);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(antPathRequestMatcher.matches(request)){
            signupManager.signup(request);
        }
        filterChain.doFilter(request,response);
    }



}
