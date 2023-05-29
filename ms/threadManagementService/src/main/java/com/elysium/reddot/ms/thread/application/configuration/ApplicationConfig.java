package com.elysium.reddot.ms.thread.application.configuration;

import com.elysium.reddot.ms.thread.domain.service.ThreadDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The ApplicationConfig class is a configuration class for the application.
 * It defines beans and their configurations for the Spring application context.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public ThreadDomainServiceImpl userService() {
        return new ThreadDomainServiceImpl();
    }


}