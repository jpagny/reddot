package com.elysium.reddot.ms.thread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The ThreadManagementServiceApplication class is the main entry point for the thread management service application.
 * It initializes and starts the Spring Boot application.
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableEurekaClient
public class ThreadManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThreadManagementServiceApplication.class, args);
    }

}
