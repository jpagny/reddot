package com.elysium.reddot.ms.topic.infrastructure.inbound.rest.processor.topic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * Component that holds instances of various topic processors.
 */
@AllArgsConstructor
@Getter
@Component
public class TopicProcessorHolder {
    private final GetAllTopicsProcessor getAllTopicsProcessor;
    private final GetTopicByIdProcessor getTopicByIdProcessor;
    private final CreateTopicProcessor createTopicProcessor;
    private final UpdateTopicProcessor updateTopicProcessor;

}