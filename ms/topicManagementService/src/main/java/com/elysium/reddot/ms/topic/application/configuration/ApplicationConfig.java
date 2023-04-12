package com.elysium.reddot.ms.topic.application.configuration;

import com.elysium.reddot.ms.topic.application.exception.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.topic.application.port.out.TopicRepositoryOutbound;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationService;
import com.elysium.reddot.ms.topic.application.service.TopicApplicationServiceImpl;
import com.elysium.reddot.ms.topic.domain.service.TopicDomainService;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.TopicRepositoryAdapter;
import com.elysium.reddot.ms.topic.infrastructure.outbound.persistence.repository.TopicJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public TopicDomainService userService() {
        return new TopicDomainService();
    }

    @Bean
    public TopicRepositoryOutbound userRepositoryOutbound(TopicJpaRepository userRepository) {
        return new TopicRepositoryAdapter(userRepository);
    }

    @Bean
    public TopicApplicationService topicApplicationService(TopicDomainService topicDomainService, TopicRepositoryOutbound userRepositoryOutbound) {
        return new TopicApplicationServiceImpl(topicDomainService, userRepositoryOutbound);
    }

    @Bean
    public CamelGlobalExceptionHandler camelGlobalExceptionHandler() {
        return new CamelGlobalExceptionHandler();
    }
}
