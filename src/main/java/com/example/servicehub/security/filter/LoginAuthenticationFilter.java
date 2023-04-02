package com.example.servicehub.security.filter;

import com.example.servicehub.config.CustomerServer;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final static String DEFAULT_FILTER_PROCESSES_URI = "/login/customer-server/code/**";
    private final static String ACCESS_TOKEN_ENDPOINT = "/auth/gettoken";
    private final static String CLIENT_ID = "clientId";
    private final CustomerServer customerServer;
    private final RestTemplate restTemplate;

    public LoginAuthenticationFilter(CustomerServer customerServer, RestTemplate restTemplate) {
        super(DEFAULT_FILTER_PROCESSES_URI);
        this.customerServer = customerServer;
        this.restTemplate = restTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String code = request.getParameter("code");

        ResponseEntity<AccessTokenDto> result = restTemplate.exchange(AccessTokenEndPointURL(), HttpMethod.POST, createRequestHeader(code), AccessTokenDto.class);

        AccessTokenDto accessTokenDto = result.getBody();

        return new PreAuthenticatedAuthenticationToken(new CustomerPrincipal(), accessTokenDto.getAccessToken());
    }

    private String AccessTokenEndPointURL() {
        return customerServer.getServer() + ACCESS_TOKEN_ENDPOINT;
    }

    private HttpEntity createRequestHeader(String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID, customerServer.getClientId());
        httpHeaders.add(HttpHeaders.AUTHORIZATION, code);
        return new HttpEntity<>(httpHeaders);
    }

    @Getter
    static class AccessTokenDto {
        private String accessToken;
    }
}
