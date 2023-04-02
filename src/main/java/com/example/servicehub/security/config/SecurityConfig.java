package com.example.servicehub.security.config;

import com.example.servicehub.config.CustomerServer;
import com.example.servicehub.security.LoginAuthenticationProvider;
import com.example.servicehub.security.filter.AuthorizationRedirectFilter;
import com.example.servicehub.security.filter.CustomerServerSignupFilter;
import com.example.servicehub.security.filter.LoginAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toStaticResources;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomerServer customerServer;
    private final RestTemplate restTemplate;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        httpSecurity.csrf();

        httpSecurity.anonymous();

        httpSecurity.authorizeRequests(auth -> {
            auth.mvcMatchers("/service/registration").hasRole("ADMIN");
            auth.mvcMatchers("/customer/**").hasRole("USER");
            auth.mvcMatchers("/comments/**").hasRole("USER");
            auth.requestMatchers(toStaticResources().atCommonLocations()).permitAll();
            auth.anyRequest().permitAll();
        });

        httpSecurity.addFilterBefore(loginAuthenticationFilter(authenticationManagerBuilder), AnonymousAuthenticationFilter.class);
        httpSecurity.addFilterBefore(new AuthorizationRedirectFilter(customerServer), AnonymousAuthenticationFilter.class);
        httpSecurity.addFilterBefore(new CustomerServerSignupFilter(customerServer), AnonymousAuthenticationFilter.class);

        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/customer-server/authorization"));
        httpSecurity
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/");

        return httpSecurity.build();
    }

    public LoginAuthenticationFilter loginAuthenticationFilter(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(new LoginAuthenticationProvider(customerServer, restTemplate));
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getOrBuild();
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(customerServer, restTemplate);
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return loginAuthenticationFilter;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

}
