package com.elysium.reddot.ms.message.application.configuration;

import com.elysium.reddot.ms.message.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.message.application.exception.handler.core.IExceptionHandler;
import com.elysium.reddot.ms.message.domain.service.MessageDomainServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public MessageDomainServiceImpl userService() {
        return new MessageDomainServiceImpl();
    }

    @Bean
    public CamelGlobalExceptionHandler camelGlobalExceptionHandler(List<IExceptionHandler> exceptionHandlers) {
        return new CamelGlobalExceptionHandler(exceptionHandlers);
    }

}