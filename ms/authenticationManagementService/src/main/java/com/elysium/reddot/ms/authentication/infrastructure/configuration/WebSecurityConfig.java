package com.elysium.reddot.ms.authentication.infrastructure.configuration;

import com.elysium.reddot.ms.authentication.infrastructure.exception.handler.CustomAuthenticationFailureHandler;
import com.elysium.reddot.ms.authentication.infrastructure.filter.CustomKeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class WebSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public LogoutHandler customKeycloakLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    @Override
    protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
        KeycloakAuthenticationProvider provider = new KeycloakAuthenticationProvider();
        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        return provider;
    }


    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .addFilterBefore(customKeycloakAuthenticationProcessingFilter(), KeycloakPreAuthActionsFilter.class)
                .authorizeRequests()
                .antMatchers("/api/auth/login", "/error", "/sso/login*").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }


    @Bean
    public CustomKeycloakAuthenticationProcessingFilter customKeycloakAuthenticationProcessingFilter() throws Exception {
        return new CustomKeycloakAuthenticationProcessingFilter(authenticationManagerBean());
    }


}