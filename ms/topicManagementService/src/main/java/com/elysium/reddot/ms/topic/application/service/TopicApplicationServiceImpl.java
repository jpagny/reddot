package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceBadValueException;
import com.elysium.reddot.ms.topic.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.domain.port.in.ITopicManagementService;
import com.elysium.reddot.ms.topic.domain.port.out.ITopicRepository;
import com.elysium.reddot.ms.topic.domain.service.TopicDomainServiceImpl;
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
public class TopicApplicationServiceImpl implements ITopicManagementService {

    private static final String RESOURCE_NAME_TOPIC = "topic";
    private final TopicDomainServiceImpl topicDomainService;
    private final ITopicRepository topicRepository;

    @Autowired
    public TopicApplicationServiceImpl(ITopicRepository topicRepository) {
        this.topicDomainService = new TopicDomainServiceImpl();
        this.topicRepository = topicRepository;
    }

    @Override
    public TopicModel getTopicById(Long id) {
        log.debug("Fetching topic with id {}", id);

        TopicModel foundTopicModel = topicRepository.findTopicById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_TOPIC, String.valueOf(id))
        );

        log.info("Successfully retrieved topic with id {}, name '{}', description '{}'",
                id, foundTopicModel.getName(), foundTopicModel.getDescription());

        return foundTopicModel;
    }

    @Override
    public List<TopicModel> getAllTopics() {
        log.info("Fetching all topics from database...");

        return topicRepository.findAllTopics()
                .parallelStream()
                .collect(Collectors.toList());
    }

    @Override
    public TopicModel createTopic(TopicModel topicToCreateModel) {

        log.debug("Creating new topic with name '{}', label '{}, description '{}'",
                topicToCreateModel.getName(),
                topicToCreateModel.getLabel(),
                topicToCreateModel.getDescription());

        Optional<TopicModel> existingTopic = topicRepository.findTopicByName(topicToCreateModel.getName());

        if (existingTopic.isPresent()) {
            throw new ResourceAlreadyExistException(RESOURCE_NAME_TOPIC, "name", topicToCreateModel.getName());
        }

        try {
            topicDomainService.validateTopicForCreation(topicToCreateModel);
        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_TOPIC, exception.getMessage());
        }

        TopicModel createdTopicModel = topicRepository.createTopic(topicToCreateModel);

        log.info("Successfully created topic with id {}, name '{}', label '{}' description '{}'",
                createdTopicModel.getId(),
                createdTopicModel.getName(),
                createdTopicModel.getLabel(),
                createdTopicModel.getDescription());

        return createdTopicModel;
    }

    @Override
    public TopicModel updateTopic(Long id, TopicModel topicToUpdateModel) {
        log.debug("Updating topic with id '{}', name '{}', label '{}', description '{}'",
                id, topicToUpdateModel.getName(), topicToUpdateModel.getLabel(), topicToUpdateModel.getDescription());

        TopicModel existingTopicModel = topicRepository.findTopicById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_TOPIC, String.valueOf(id))
        );

        try {
            TopicModel topicModelWithUpdates = topicDomainService.updateExistingTopicWithUpdates(existingTopicModel, topicToUpdateModel);

            TopicModel updatedTopicModel = topicRepository.updateTopic(topicModelWithUpdates);

            log.info("Successfully updated topic with id '{}', name '{}', label'{}, description '{}'",
                    updatedTopicModel.getId(),
                    updatedTopicModel.getName(),
                    updatedTopicModel.getLabel(),
                    updatedTopicModel.getDescription());

            return updatedTopicModel;

        } catch (Exception ex) {
            throw new ResourceBadValueException(RESOURCE_NAME_TOPIC, ex.getMessage());

        }
    }

    @Override
    public TopicModel deleteTopicById(Long id) throws ResourceNotFoundException {
        log.debug("Deleting topic with id {}", id);

        TopicModel topicToDelete = topicRepository.findTopicById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_TOPIC, String.valueOf(id))
        );

        topicRepository.deleteTopic(id);

        log.info("Successfully deleted topic with id '{}', name '{}', description '{}'",
                topicToDelete.getId(), topicToDelete.getName(), topicToDelete.getDescription());

        return topicToDelete;
    }

}