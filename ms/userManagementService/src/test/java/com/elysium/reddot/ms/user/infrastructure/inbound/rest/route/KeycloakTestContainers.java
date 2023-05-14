package com.elysium.reddot.ms.user.infrastructure.inbound.rest.route;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public abstract class KeycloakTestContainers {

    static {
        KeycloakContainer keycloak = new KeycloakContainer()
                .withRealmImportFile("realm-test-reddot-export.json");
        keycloak.setPortBindings(List.of("11145:8080"));

        keycloak.start();
    }
}