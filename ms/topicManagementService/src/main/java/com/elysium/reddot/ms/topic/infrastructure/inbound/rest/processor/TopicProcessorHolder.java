package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class TopicProcessorHolder {
    private final GetAllTopicsProcessor getAllTopicsProcessor;
    private final GetTopicByIdProcessor getTopicByIdProcessor;
    private final CreateTopicProcessor createTopicProcessor;
    private final UpdateTopicProcessor updateTopicProcessor;
    private final DeleteTopicProcessor deleteTopicProcessor;

}