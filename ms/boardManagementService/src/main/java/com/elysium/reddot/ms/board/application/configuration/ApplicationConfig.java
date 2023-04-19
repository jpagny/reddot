package com.elysium.reddot.ms.board.application.configuration;

import com.elysium.reddot.ms.board.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.board.application.exception.handler.core.IExceptionHandler;
import com.elysium.reddot.ms.board.domain.service.BoardDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class ApplicationConfig {

    @Bean
    public BoardDomainService boardService() {
        return new BoardDomainService();
    }

    @Bean
    public CamelGlobalExceptionHandler camelGlobalExceptionHandler(List<IExceptionHandler> exceptionHandlers) {
        return new CamelGlobalExceptionHandler(exceptionHandlers);
    }
}