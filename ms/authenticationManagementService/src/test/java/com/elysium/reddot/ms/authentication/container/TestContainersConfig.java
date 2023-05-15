package com.elysium.reddot.ms.authentication.container;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public abstract class TestContainersConfig {

    private static final KeycloakContainer keycloakContainer;

    static {
        keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:21.1.1").withRealmImportFile("realm-test-reddot-export.json")
                .withReuse(true);
        keycloakContainer.setPortBindings(List.of("11145:8080"));

    }

    public static KeycloakContainer getKeycloakContainer() {
        return keycloakContainer;
    }


}
