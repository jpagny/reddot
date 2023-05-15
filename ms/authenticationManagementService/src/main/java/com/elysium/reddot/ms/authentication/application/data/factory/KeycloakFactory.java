package com.elysium.reddot.ms.authentication.application.data.factory;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakFactory {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.resource}")
    private String userClientId;

    @Value("${keycloak.credentials.secret}")
    private String userClientSecret;

    public Keycloak buildKeycloak(String username, String password) {

        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType("password")
                .clientId(userClientId)
                .clientSecret(userClientSecret)
                .username(username)
                .password(password)
                .build();
    }
}
