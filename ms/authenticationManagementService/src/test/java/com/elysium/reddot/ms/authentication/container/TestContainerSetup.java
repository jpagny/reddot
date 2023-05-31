package com.elysium.reddot.ms.authentication.container;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestContainerSetup {

    @BeforeAll
    static void setUp() {
        TestContainersConfig.getKeycloakContainer().start();
    }

    @AfterAll
    static void tearDown() {
        TestContainersConfig.getKeycloakContainer().stop();
    }

}