package com.example.servicehub.security.authentication;

import com.example.servicehub.config.CustomerServer;
import com.example.servicehub.domain.customer.ProviderType;
import com.example.servicehub.domain.customer.RoleType;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private final static String CUSTOMER_ENDPOINT = "/api/v1/customer";
    private static final String CLIENT_ID = "clientId";
    private final CustomerServer customerServer;
    private final RestTemplate restTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = getToken(authentication);

        CustomerDto customer = restTemplate.exchange(customerServer.getServer() + CUSTOMER_ENDPOINT, HttpMethod.POST, createRequestHeader(token), CustomerDto.class).getBody();

        CustomerPrincipal customerPrincipal = customer.toPrincipal();

        return new PreAuthenticatedAuthenticationToken(customerPrincipal, token, customerPrincipal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private String getToken(Authentication authentication) {
        return String.valueOf(authentication.getCredentials());
    }

    private HttpEntity createRequestHeader(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID, customerServer.getClientId());
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(httpHeaders);
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

        CustomerPrincipal toPrincipal() {
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
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority(roleType.getName())))
                    .build();
        }
    }

}
