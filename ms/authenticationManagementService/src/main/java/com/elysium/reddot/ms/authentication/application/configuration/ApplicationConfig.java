package com.elysium.reddot.ms.authentication.application.configuration;

import com.elysium.reddot.ms.authentication.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.authentication.application.exception.handler.core.IExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public CamelGlobalExceptionHandler camelGlobalExceptionHandler(List<IExceptionHandler> exceptionHandlers) {
        return new CamelGlobalExceptionHandler(exceptionHandlers);
    }
}
