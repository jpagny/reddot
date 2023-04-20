package com.elysium.reddot.ms.topic.application.configuration;

import com.elysium.reddot.ms.topic.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.topic.application.exception.handler.core.IExceptionHandler;
import com.elysium.reddot.ms.topic.domain.service.TopicDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public TopicDomainServiceImpl userService() {
        return new TopicDomainServiceImpl();
    }

    @Bean
    public CamelGlobalExceptionHandler camelGlobalExceptionHandler(List<IExceptionHandler> exceptionHandlers) {
        return new CamelGlobalExceptionHandler(exceptionHandlers);
    }
}
