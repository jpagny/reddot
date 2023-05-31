package com.elysium.reddot.ms.topic.application.configuration;

import com.elysium.reddot.ms.topic.domain.service.TopicDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class holds the bean definitions for the application.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public TopicDomainServiceImpl userService() {
        return new TopicDomainServiceImpl();
    }

}
