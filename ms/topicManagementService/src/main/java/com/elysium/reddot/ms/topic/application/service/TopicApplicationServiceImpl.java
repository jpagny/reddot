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

    private static final String RESOURCE_NAME_TOPIC = "topic";
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
                () -> new ResourceNotFoundException(RESOURCE_NAME_TOPIC, String.valueOf(id))
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
    public TopicDTO createTopic(TopicDTO topicToCreateDTO) {

        log.debug("Creating new topic with name '{}', label '{}, description '{}'",
                topicToCreateDTO.getName(),
                topicToCreateDTO.getLabel(),
                topicToCreateDTO.getDescription());

        Optional<TopicDTO> existingTopic = topicRepository.findTopicByName(topicToCreateDTO.getName());

        if (existingTopic.isPresent()) {
            throw new ResourceAlreadyExistException(RESOURCE_NAME_TOPIC, "name", topicToCreateDTO.getName());
        }

        TopicModel topicModel = TopicApplicationMapper.toModel(topicToCreateDTO);

        try {
            domainService.validateBuildTopic(topicModel);
        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_TOPIC, exception.getMessage());
        }

        TopicDTO topicCreatedDTO = topicRepository.createTopic(topicToCreateDTO);

        log.info("Successfully created topic with id {}, name '{}', label '{}' description '{}'",
                topicCreatedDTO.getId(),
                topicCreatedDTO.getName(),
                topicCreatedDTO.getLabel(),
                topicCreatedDTO.getDescription());

        return topicCreatedDTO;
    }

    @Override
    public TopicDTO updateTopic(Long id, TopicDTO topicToUpdateDTO) {
        log.debug("Updating topic with id '{}', name '{}', label '{}', description '{}'",
                id, topicToUpdateDTO.getName(), topicToUpdateDTO.getLabel(), topicToUpdateDTO.getDescription());

        TopicDTO existingTopicDTO = topicRepository.findTopicById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_TOPIC, String.valueOf(id))
        );

        TopicModel existingTopicModel = TopicApplicationMapper.toModel(existingTopicDTO);
        TopicModel topicToUpdateModel = TopicApplicationMapper.toModel(topicToUpdateDTO);

        try {
            TopicModel topicUpdatedModel = domainService.updateTopic(existingTopicModel, topicToUpdateModel);
            TopicDTO topicUpdatedDTO = TopicApplicationMapper.toDTO(topicUpdatedModel);

            topicUpdatedDTO = topicRepository.updateTopic(topicUpdatedDTO);

            log.info("Successfully updated topic with id '{}', name '{}', label'{}, description '{}'",
                    topicUpdatedDTO.getId(),
                    topicUpdatedDTO.getName(),
                    topicUpdatedDTO.getLabel(),
                    topicUpdatedDTO.getDescription());

            return topicUpdatedDTO;

        } catch (Exception ex) {
            throw new ResourceBadValueException(RESOURCE_NAME_TOPIC, ex.getMessage());

        }
    }

    @Override
    public void deleteTopicById(Long id) throws ResourceNotFoundException {
        log.debug("Deleting topic with id {}", id);

        TopicDTO topicToDelete = topicRepository.findTopicById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_TOPIC, String.valueOf(id))
        );

        topicRepository.deleteTopic(id);

        log.info("Successfully deleted topic with id '{}', name '{}', description '{}'",
                topicToDelete.getId(), topicToDelete.getName(), topicToDelete.getDescription());
    }

}