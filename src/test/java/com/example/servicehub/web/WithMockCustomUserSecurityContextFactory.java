package com.example.servicehub.web;

import com.example.servicehub.domain.ProviderType;
import com.example.servicehub.domain.RoleType;
import com.example.servicehub.security.authentication.ClientPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private final static String prefix = "ROLE_";

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        String username = customUser.username();

        String role = prefix + customUser.role();

        ClientPrincipal clientPrincipal = new ClientPrincipal(
                customUser.id(),username,"test", ProviderType.KEYCLOAK, RoleType.of(role), Collections.singletonList(new SimpleGrantedAuthority(role)),"test","test","test"
        );
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(clientPrincipal,clientPrincipal.getPassword(),clientPrincipal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }







}
