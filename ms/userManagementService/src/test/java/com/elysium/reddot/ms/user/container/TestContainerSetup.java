package com.elysium.reddot.ms.user.container;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestContainerSetup {

    @BeforeAll
    static void setUp() {
        TestContainersConfig.getRabbitMQContainer().start();
        TestContainersConfig.getKeycloakContainer().start();
    }

    @AfterAll
    static void tearDown() {
        TestContainersConfig.getRabbitMQContainer().stop();
        TestContainersConfig.getKeycloakContainer().stop();
    }

}