package com.elysium.reddot.ms.thread.application.configuration;

import com.elysium.reddot.ms.thread.domain.service.ThreadDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ThreadDomainServiceImpl userService() {
        return new ThreadDomainServiceImpl();
    }


}