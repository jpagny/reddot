package com.elysium.reddot.edge.springgatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringGatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringGatewayServerApplication.class, args);
	}

}
