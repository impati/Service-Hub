package com.example.servicehub.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomClientManager userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ClientContext clientContext = (ClientContext) userDetailsService.loadUserByUsername(authentication.getName());
        if(matchPassword(authentication.getCredentials(),clientContext.getPassword())){
            return new UsernamePasswordAuthenticationToken(clientContext,authentication.getCredentials(),clientContext.getAuthorities());
        }
        throw new BadCredentialsException("Bad CredentialsException");
    }

    private boolean matchPassword(Object rawPassword, String encodedPassword){
        if(passwordEncoder.matches((CharSequence) rawPassword,encodedPassword))return true;
        return false;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
