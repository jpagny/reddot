package com.elysium.reddot.ms.message.application.configuration;

import com.elysium.reddot.ms.message.domain.service.MessageDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public MessageDomainServiceImpl userService() {
        return new MessageDomainServiceImpl();
    }

}
