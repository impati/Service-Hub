package com.example.servicehub.security;

import com.example.servicehub.domain.ProviderType;
import com.example.servicehub.domain.RoleType;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.security.config.CustomerServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private final static String CUSTOMER_ENDPOINT = "/api/v1/customer";

    private final CustomerServer customerServer;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        PreAuthenticatedAuthenticationToken authenticatedAuthenticationToken = (PreAuthenticatedAuthenticationToken) authentication;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("clientId", customerServer.getClientId());
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authenticatedAuthenticationToken.getCredentials());

        CustomerDto customer = restTemplate.exchange(customerServer.getTargetUrl() + CUSTOMER_ENDPOINT, HttpMethod.POST, new HttpEntity<>(httpHeaders), CustomerDto.class).getBody();

        return new PreAuthenticatedAuthenticationToken(customer.toEntity(), authenticatedAuthenticationToken.getCredentials());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class CustomerDto {
        private Long id;
        private String username;
        private String nickname;
        private String email;
        private ProviderType providerType;
        private RoleType roleType;
        private String blogUrl;
        private String profileImageUrl;
        private String introduceComment;

        CustomerPrincipal toEntity() {
            return CustomerPrincipal.builder()
                    .id(id)
                    .username(username)
                    .nickname(nickname)
                    .email(email)
                    .providerType(providerType)
                    .roleType(roleType)
                    .blogUrl(blogUrl)
                    .profileImageUrl(profileImageUrl)
                    .introduceComment(introduceComment)
                    .build();
        }
    }

}
