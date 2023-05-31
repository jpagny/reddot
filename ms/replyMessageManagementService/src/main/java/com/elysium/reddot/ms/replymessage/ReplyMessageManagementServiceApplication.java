package com.elysium.reddot.ms.replymessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The ReplyMessageManagementServiceApplication class is the entry point for the Reply Message Management Service.
 * It bootstraps the Spring Boot application.
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableEurekaClient
public class ReplyMessageManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReplyMessageManagementServiceApplication.class, args);
    }

}
