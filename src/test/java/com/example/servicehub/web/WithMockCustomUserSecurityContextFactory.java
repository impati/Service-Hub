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

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        String username = customUser.username();
        ClientPrincipal clientPrincipal = new ClientPrincipal(
                customUser.id(),username,"test", ProviderType.KEYCLOAK, RoleType.ADMIN, Collections.singletonList(new SimpleGrantedAuthority(RoleType.ADMIN.getName())),"test","test","test"
        );
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(clientPrincipal,clientPrincipal.getPassword(),clientPrincipal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }

}
