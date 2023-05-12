package com.elysium.reddot.ms.user.application.configuration;

import com.elysium.reddot.ms.user.domain.service.IUserDomainService;
import com.elysium.reddot.ms.user.domain.service.UserDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public IUserDomainService userService() {
        return new UserDomainServiceImpl();
    }

}
