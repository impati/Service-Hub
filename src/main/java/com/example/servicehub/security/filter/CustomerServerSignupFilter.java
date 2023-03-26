package com.example.servicehub.security.filter;

import com.example.servicehub.security.config.CustomerServer;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerServerSignupFilter extends OncePerRequestFilter {
    private static final String DEFAULT_AUTHORIZATION_REQUEST_BASE_URI = "/customer-server/signup";
    private static final String CLIENT_ID = "clientId";
    private static final String LOGIN_URI = "/signup";

    private final RequestMatcher requestMatcher;
    private final RedirectStrategy redirectStrategy;
    private final CustomerServer customerServer;

    public CustomerServerSignupFilter(CustomerServer customerServer) {
        this.requestMatcher = new AntPathRequestMatcher(DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        this.redirectStrategy = new DefaultRedirectStrategy();
        this.customerServer = customerServer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            redirectStrategy.sendRedirect(request, response, getRedirectUrl());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getRedirectUrl() {
        return UriComponentsBuilder
                .fromUriString(customerServer.getTargetUrl())
                .path(LOGIN_URI)
                .queryParam(CLIENT_ID, customerServer.getClientId())
                .build()
                .toString();
    }

}
