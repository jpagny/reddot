package com.elysium.reddot.ms.user.infrastructure.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak-admin.username}")
    private String keycloakAdminUsername;

    @Value("${keycloak-admin.password}")
    private String keycloakAdminPassword;

    @Value("${keycloak-admin.client-id}")
    private String keycloakAdminClientId;

    @Value("${keycloak-admin.client-secret}")
    private String keycloakAdminClientSecret;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .username(keycloakAdminUsername)
                .password(keycloakAdminPassword)
                .clientId(keycloakAdminClientId)
                .clientSecret(keycloakAdminClientSecret)
                .build();
    }
}
