package com.reddot.ms.topic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TopicApplicationTests {

    @Test
    void contextLoads() {
        Assertions.assertDoesNotThrow(() -> {
            new SpringApplicationBuilder(TopicApplication.class).run();
        });
    }

}
