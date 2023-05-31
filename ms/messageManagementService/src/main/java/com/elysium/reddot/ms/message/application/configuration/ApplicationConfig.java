package com.elysium.reddot.ms.message.application.configuration;

import com.elysium.reddot.ms.message.domain.service.MessageDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used for configuring the beans that will be used in the application.
 * It defines a series of bean-producing methods that will be managed by the Spring container.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public MessageDomainServiceImpl userService() {
        return new MessageDomainServiceImpl();
    }

}
