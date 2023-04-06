package com.reddot.ms.topic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

class TopicApplicationTest {

    @Test
    void contextLoads() {
        Assertions.assertDoesNotThrow(() -> {
            new SpringApplicationBuilder(TopicApplication.class).run();
        });
    }

}
