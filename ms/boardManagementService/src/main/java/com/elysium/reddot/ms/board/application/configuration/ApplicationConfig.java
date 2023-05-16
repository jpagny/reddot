package com.elysium.reddot.ms.board.application.configuration;

import com.elysium.reddot.ms.board.domain.service.BoardDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public BoardDomainServiceImpl userService() {
        return new BoardDomainServiceImpl();
    }

}
