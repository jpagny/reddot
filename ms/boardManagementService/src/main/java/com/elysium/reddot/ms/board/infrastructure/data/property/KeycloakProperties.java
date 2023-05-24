package com.elysium.reddot.ms.board.infrastructure.data.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class KeycloakProperties {
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.resource}")
    private String resource;

    @Value("${keycloak.credentials.secret}")
    private String credentialsSecret;

}
