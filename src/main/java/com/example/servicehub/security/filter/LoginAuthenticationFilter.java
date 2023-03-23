package com.example.servicehub.security.filter;

import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.security.config.CustomerServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private final CustomerServer customerServer;

    public LoginAuthenticationFilter(CustomerServer customerServer) {
        super(DEFAULT_FILTER_PROCESSES_URI);
        this.customerServer = customerServer;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String code = request.getParameter("code");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("clientId", customerServer.getClientId());
        httpHeaders.add(HttpHeaders.AUTHORIZATION, code);
        ResponseEntity<AccessTokenDto> exchange = restTemplate.exchange(customerServer.getTargetUrl() + ACCESS_TOKEN_ENDPOINT, HttpMethod.POST, new HttpEntity<>(httpHeaders), AccessTokenDto.class);

        AccessTokenDto accessTokenDto = exchange.getBody();

        return new PreAuthenticatedAuthenticationToken(new CustomerPrincipal(), accessTokenDto.accessToken);
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class AccessTokenDto {
        private String accessToken;
    }
}
