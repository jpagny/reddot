package com.elysium.reddot.ms.thread.application.configuration;

import com.elysium.reddot.ms.thread.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.thread.application.exception.handler.core.IExceptionHandler;
import com.elysium.reddot.ms.thread.domain.service.ThreadDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public ThreadDomainServiceImpl userService() {
        return new ThreadDomainServiceImpl();
    }

    @Bean
    public CamelGlobalExceptionHandler camelGlobalExceptionHandler(List<IExceptionHandler> exceptionHandlers) {
        return new CamelGlobalExceptionHandler(exceptionHandlers);
    }
}
