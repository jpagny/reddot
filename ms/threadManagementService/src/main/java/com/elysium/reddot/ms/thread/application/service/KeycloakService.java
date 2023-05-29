package com.elysium.reddot.ms.thread.application.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
@Slf4j
public class KeycloakService {

    public String getUserId() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof KeycloakAuthenticationToken)) {
            throw new AuthenticationException("Must be authenticated by Keycloak");
        }

        KeycloakAuthenticationToken keycloakAuthentication = (KeycloakAuthenticationToken) authentication;
        KeycloakPrincipal principal = (KeycloakPrincipal) keycloakAuthentication.getPrincipal();
        AccessToken token = principal.getKeycloakSecurityContext().getToken();

        return token.getSubject();
    }

}
