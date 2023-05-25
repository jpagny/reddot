package com.elysium.reddot.ms.topic.application.configuration;

import com.elysium.reddot.ms.topic.domain.service.TopicDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public TopicDomainServiceImpl userService() {
        return new TopicDomainServiceImpl();
    }

}
