package com.elysium.reddot.ms.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The main class that starts the Message Management Service application.
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableEurekaClient
public class MessageManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageManagementServiceApplication.class, args);
    }

}
