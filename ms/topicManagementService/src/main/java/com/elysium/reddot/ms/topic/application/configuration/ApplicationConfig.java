package com.elysium.reddot.ms.topic.application.configuration;

import com.elysium.reddot.ms.topic.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.core.ExceptionHandler;
import com.elysium.reddot.ms.topic.domain.service.TopicDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public TopicDomainService userService() {
        return new TopicDomainService();
    }

    @Bean
    public CamelGlobalExceptionHandler camelGlobalExceptionHandler(List<ExceptionHandler> exceptionHandlers) {
        return new CamelGlobalExceptionHandler(exceptionHandlers);
    }
}
