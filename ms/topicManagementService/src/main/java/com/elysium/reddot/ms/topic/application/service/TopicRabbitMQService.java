package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.domain.port.outbound.ITopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This service is responsible for handling RabbitMQ related operations for Topic objects.
 */
@Service
@RequiredArgsConstructor
public class TopicRabbitMQService {

    private final ITopicRepository topicRepository;

    /**
     * Checks whether a Topic with the given ID exists in the database.
     *
     * @param id The ID of the Topic to check.
     * @return true if a Topic with the given ID exists, false otherwise.
     */
    public boolean checkTopicIdExists(Long id) {
        Optional<TopicModel> topicModel = topicRepository.findTopicById(id);
        return topicModel.isPresent();
    }
}
