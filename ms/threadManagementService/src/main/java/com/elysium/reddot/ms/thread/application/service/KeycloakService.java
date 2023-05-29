package com.elysium.reddot.ms.thread.application.service;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
@Slf4j
@SuppressWarnings("unchecked")
public class KeycloakService {

    public String getUserId() throws AuthenticationException {
        log.debug("Fetching authentication");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.debug("Instance of : " + authentication.getClass());

        if (!(authentication instanceof KeycloakAuthenticationToken)) {
            throw new AuthenticationException("Must be authenticated by Keycloak");
        }

        KeycloakAuthenticationToken keycloakAuthentication = (KeycloakAuthenticationToken) authentication;
        KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) keycloakAuthentication.getPrincipal();
        AccessToken token = principal.getKeycloakSecurityContext().getToken();

        return token.getSubject();
    }

}