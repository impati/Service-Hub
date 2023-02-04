package com.example.servicehub.security.filter;

import com.example.servicehub.security.authentication.CustomOAuth2UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.RequestMatcherRedirectFilter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientManager.authorize(getOAuth2AuthorizeRequest(request,response));

        if(isExist(oAuth2AuthorizedClient)) {

            OAuth2User oauth2User = customOAuth2UserService.loadUser(new OAuth2UserRequest(
                    oAuth2AuthorizedClient.getClientRegistration(), oAuth2AuthorizedClient.getAccessToken()));

            return new OAuth2AuthenticationToken(oauth2User, oauth2User.getAuthorities(), REGISTRATION_ID);
        }
        return null;
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

}
