package com.elysium.reddot.ms.topic.application.config;

import com.elysium.reddot.ms.topic.application.service.TopicApplicationService;
import com.elysium.reddot.ms.topic.domain.service.TopicDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public TopicDomainService topicDomainService() {
        return new TopicDomainService();
    }

    @Bean
    public TopicApplicationService topicApplicationService(TopicDomainService topicDomainService) {
        return new TopicApplicationService(topicDomainService);
    }
}