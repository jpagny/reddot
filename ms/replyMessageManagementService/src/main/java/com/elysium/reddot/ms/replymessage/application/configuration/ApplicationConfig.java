package com.elysium.reddot.ms.replymessage.application.configuration;

import com.elysium.reddot.ms.replymessage.domain.service.ReplyMessageDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ReplyMessageDomainServiceImpl userService() {
        return new ReplyMessageDomainServiceImpl();
    }

}
