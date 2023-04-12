package com.elysium.reddot.ms.topic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TopicManagementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TopicManagementServiceApplication.class, args);
    }
}
