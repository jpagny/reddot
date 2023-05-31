package com.elysium.reddot.ms.message.container;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.RabbitMQContainer;

import java.util.List;

@SpringBootTest
public abstract class TestContainersConfig {

    private static final RabbitMQContainer rabbitMQContainer;
    private static final KeycloakContainer keycloakContainer;

    static {
        rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.11.16-management-alpine")
                .withReuse(true);
        rabbitMQContainer.setPortBindings(List.of("11148:5672"));

        keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:21.1.1")
                .withRealmImportFile("realm-test-reddot-export.json")
                .withReuse(true);
        keycloakContainer.setPortBindings(List.of("11003:8080"));
    }

    public static RabbitMQContainer getRabbitMQContainer() {
        return rabbitMQContainer;
    }

    public static KeycloakContainer getKeycloakContainer() {
        return keycloakContainer;
    }


}
