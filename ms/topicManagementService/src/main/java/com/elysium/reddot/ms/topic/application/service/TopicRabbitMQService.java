package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.domain.port.outbound.ITopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicRabbitMQService {

    private final ITopicRepository topicRepository;

    public boolean checkTopicIdExists(Long id) {
        Optional<TopicModel> topicModel = topicRepository.findTopicById(id);
        return topicModel.isPresent();
    }
}
