package com.elysium.reddot.ms.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
public class AuthenticationManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationManagementServiceApplication.class, args);
    }

}
