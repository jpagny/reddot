package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.application.data.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.data.mapper.TopicApplicationMapper;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceBadValueException;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.application.port.in.TopicManagement;
import com.elysium.reddot.ms.topic.application.port.out.TopicRepositoryOutbound;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class TopicApplicationServiceImpl implements TopicApplicationService {

    private static final String MESSAGE_TOPIC = "topic";
    private final TopicManagement domainService;
    private final TopicRepositoryOutbound topicRepository;

    @Autowired
    public TopicApplicationServiceImpl(TopicManagement domainService, TopicRepositoryOutbound userRepositoryOutbound) {
        this.domainService = domainService;
        this.topicRepository = userRepositoryOutbound;
    }

    public TopicDTO getTopicById(Long id) {
        log.debug("Fetching topic with id {}", id);

        TopicDTO topicDTO = topicRepository.findTopicById(id).orElseThrow(
                () -> new ResourceNotFoundException(MESSAGE_TOPIC, String.valueOf(id))
        );

        log.info("Successfully retrieved topic with id {}, name '{}', description '{}'",
                id, topicDTO.getName(), topicDTO.getDescription());

        return topicDTO;
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        log.info("Fetching all topics from database...");

        return topicRepository.findAllTopics()
                .parallelStream()
                .collect(Collectors.toList());
    }

    @Override
    public TopicDTO createTopic(TopicDTO topicDto) {

        log.debug("Creating new topic with name '{}', description '{}'", topicDto.getName(), topicDto.getDescription());

        Optional<TopicDTO> existingTopic = topicRepository.findTopicByName(topicDto.getName());

        if (existingTopic.isPresent()) {
            throw new ResourceAlreadyExistException(MESSAGE_TOPIC, "name", topicDto.getName());
        }

        TopicModel topicModel = TopicApplicationMapper.toModel(topicDto);

        try {
            domainService.validateTopic(topicModel);
        } catch (Exception exception) {
            throw new ResourceBadValueException(MESSAGE_TOPIC, exception.getMessage());
        }

        TopicDTO topicCreated = topicRepository.createTopic(topicDto);

        log.info("Successfully created topic with id {}, name '{}', description '{}'",
                topicCreated.getId(), topicCreated.getName(), topicCreated.getDescription());

        return topicCreated;
    }

    @Override
    public TopicDTO updateTopic(Long id, TopicDTO topicDto) {
        log.debug("Updating topic with id '{}', label '{}', description '{}'",
                id, topicDto.getLabel(), topicDto.getDescription());

        TopicDTO existingTopic = topicRepository.findTopicById(id).orElseThrow(
                () -> new ResourceNotFoundException(MESSAGE_TOPIC, String.valueOf(id))
        );

        TopicModel existingTopicModel = TopicApplicationMapper.toModel(existingTopic);
        TopicModel topicToUpdateModel = TopicApplicationMapper.toModel(topicDto);

        try {
            domainService.validateTopic(topicToUpdateModel);
        } catch (Exception ex) {
            throw new ResourceBadValueException("topic", ex.getMessage());
        }

        TopicModel topicUpdatedModel = domainService.updateTopicProperties(existingTopicModel, topicToUpdateModel);
        TopicDTO topicUpdatedDTO = TopicApplicationMapper.toDTO(topicUpdatedModel);

        TopicDTO topicUpdated = topicRepository.updateTopic(topicUpdatedDTO);

        log.info("Successfully updated topic with id '{}', name '{}', description '{}'",
                topicUpdated.getId(),
                topicUpdated.getName(),
                topicUpdated.getDescription());

        return topicUpdated;
    }

    @Override
    public void deleteTopicById(Long id) throws ResourceNotFoundException {
        log.debug("Deleting topic with id {}", id);

        TopicDTO topicToDelete = topicRepository.findTopicById(id).orElseThrow(
                () -> new ResourceNotFoundException(MESSAGE_TOPIC, String.valueOf(id))
        );

        topicRepository.deleteTopic(id);

        log.info("Successfully deleted topic with id '{}', name '{}', description '{}'",
                topicToDelete.getId(), topicToDelete.getName(), topicToDelete.getDescription());
    }

}