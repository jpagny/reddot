package com.elysium.reddot.ms.replymessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

@SpringBootApplication
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReplyMessageManagementServiceApplicationIT {

    public static void main(String[] args) {
        SpringApplication.run(ReplyMessageManagementServiceApplication.class, args);
    }

}
