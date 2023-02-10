package com.example.servicehub.security.handler;

import com.example.servicehub.util.ProjectUtils;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PasswordGrantTypeLoginFailureHandler implements AuthenticationFailureHandler {

    private final RedirectStrategy redirectStrategy;
    private final String defaultFailureUrl = ProjectUtils.getDomain() + "/login?error=";

    public PasswordGrantTypeLoginFailureHandler() {
        this.redirectStrategy = new DefaultRedirectStrategy();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        redirectStrategy.sendRedirect(request,response,defaultFailureUrl + "true");
    }

}
