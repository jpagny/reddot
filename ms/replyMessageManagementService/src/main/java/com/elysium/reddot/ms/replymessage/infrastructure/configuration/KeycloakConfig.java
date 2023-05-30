package com.elysium.reddot.ms.replymessage.infrastructure.configuration;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The KeycloakConfig class is a configuration class for Keycloak integration.
 * It provides a bean for resolving Keycloak configuration for Spring Boot.
 */
@Configuration
public class KeycloakConfig {

    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

}