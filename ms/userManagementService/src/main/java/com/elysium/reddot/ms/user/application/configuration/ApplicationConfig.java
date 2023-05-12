package com.elysium.reddot.ms.user.application.configuration;

import com.elysium.reddot.ms.user.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.user.application.exception.handler.core.IExceptionHandler;
import com.elysium.reddot.ms.user.domain.service.IUserDomainService;
import com.elysium.reddot.ms.user.domain.service.UserDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public IUserDomainService userService() {
        return new UserDomainServiceImpl();
    }

    @Bean
    public CamelGlobalExceptionHandler camelGlobalExceptionHandler(List<IExceptionHandler> exceptionHandlers) {
        return new CamelGlobalExceptionHandler(exceptionHandlers);
    }
}
