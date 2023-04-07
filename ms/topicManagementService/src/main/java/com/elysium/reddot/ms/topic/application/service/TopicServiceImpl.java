package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.application.port.in.TopicUseCase;
import com.elysium.reddot.ms.topic.application.port.out.TopicRepository;
import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicUseCase {

    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAll();
    }

    @Override
    public TopicDTO getTopicById(Long id) throws ResourceNotFoundException {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("",""));
    }

    @Override
    public Topic createTopic(Topic topic) throws InvalidTopicException {
        // Validate input parameters
        if (topic == null) {
            throw new InvalidTopicException("Topic is null");
        }
        if (topic.getName() == null || topic.getName().isEmpty()) {
            throw new InvalidTopicException("Topic name is invalid");
        }

        // Save the new topic and return it
        return topicRepository.save(topic);
    }

    @Override
    public Topic updateTopic(Topic topic) throws ResourceNotFoundException {
        // Validate input parameters
        if (topic == null) {
            throw new InvalidTopicException("Topic is null");
        }
        if (topic.getId() == null) {
            throw new InvalidTopicException("Topic ID is null");
        }
        if (topic.getName() == null || topic.getName().isEmpty()) {
            throw new InvalidTopicException("Topic name is invalid");
        }

        // Check if the topic exists
        topicRepository.findById(topic.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        // Save the updated topic and return it
        return topicRepository.save(topic);
    }

    @Override
    public void deleteTopicById(Long id) throws ResourceNotFoundException {
        // Check if the topic exists
        topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("",""));

        // Delete the topic
        topicRepository.deleteById(id);
    }
