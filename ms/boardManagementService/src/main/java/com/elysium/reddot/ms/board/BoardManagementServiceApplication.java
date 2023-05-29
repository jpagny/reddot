package com.elysium.reddot.ms.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main class for the Board Management Service application.
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableEurekaClient
public class BoardManagementServiceApplication {

    /**
     * Main method to start the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BoardManagementServiceApplication.class, args);
    }

}
