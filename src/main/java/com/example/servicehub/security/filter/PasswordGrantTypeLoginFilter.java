package com.example.servicehub.security.filter;

import com.example.servicehub.security.authentication.CustomOAuth2UserService;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PasswordGrantTypeLoginFilter extends AbstractAuthenticationProcessingFilter {

    public static final String DEFAULT_FILTER_PROCESSES_URI = "/password/keycloak/login/**";
    public static final String SIGNUP_URI = "/keycloak/signup/**";
    private static final String REGISTRATION_ID = "keycloak";
    private final DefaultOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    private final CustomOAuth2UserService customOAuth2UserService;

    public PasswordGrantTypeLoginFilter(DefaultOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager, CustomOAuth2UserService customOAuth2UserService) {
        super(new OrRequestMatcher(new AntPathRequestMatcher(DEFAULT_FILTER_PROCESSES_URI),new AntPathRequestMatcher(SIGNUP_URI)));
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, ServletException, IOException {
        try {
            return attemptAuthenticationOrThrow(request,response);
        }catch (ClientAuthorizationException e){
            getFailureHandler().onAuthenticationFailure(request,response,new InsufficientAuthenticationException("invalid username or password"));
        }
        return null;
    }


    private Authentication attemptAuthenticationOrThrow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientManager.authorize(getOAuth2AuthorizeRequest(request, response));

        if (isExist(oAuth2AuthorizedClient)) {
            OAuth2User oauth2User = customOAuth2UserService.loadUser(new OAuth2UserRequest(
                    oAuth2AuthorizedClient.getClientRegistration(), oAuth2AuthorizedClient.getAccessToken()));

            return new OAuth2AuthenticationToken(oauth2User, oauth2User.getAuthorities(), REGISTRATION_ID);
        }

        throw new ClientAuthorizationException(new OAuth2Error("keycloak Error"),REGISTRATION_ID);
    }

    private OAuth2AuthorizeRequest getOAuth2AuthorizeRequest(HttpServletRequest request,HttpServletResponse response){
        return OAuth2AuthorizeRequest
                .withClientRegistrationId(REGISTRATION_ID)
                .principal(SecurityContextHolder.getContext().getAuthentication())
                .attribute(HttpServletRequest.class.getName(), request)
                .attribute(HttpServletResponse.class.getName(), response)
                .build();
    }

    private boolean isExist(OAuth2AuthorizedClient oAuth2AuthorizedClient){
        if(oAuth2AuthorizedClient != null) return true;
        return false;
    }


    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }

}
