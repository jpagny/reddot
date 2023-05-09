package com.elysium.reddot.ms.replymessage.application.configuration;

import com.elysium.reddot.ms.replymessage.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.replymessage.application.exception.handler.core.IExceptionHandler;
import com.elysium.reddot.ms.replymessage.domain.service.ReplyMessageDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public ReplyMessageDomainServiceImpl userService() {
        return new ReplyMessageDomainServiceImpl();
    }

    @Bean
    public CamelGlobalExceptionHandler camelGlobalExceptionHandler(List<IExceptionHandler> exceptionHandlers) {
        return new CamelGlobalExceptionHandler(exceptionHandlers);
    }
}
