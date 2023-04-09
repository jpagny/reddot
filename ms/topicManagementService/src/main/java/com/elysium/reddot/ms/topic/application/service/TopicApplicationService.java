package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.application.port.in.TopicUseCase;
import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.application.service.mapper.TopicApplicationMapper;
import com.elysium.reddot.ms.topic.domain.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.domain.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import com.elysium.reddot.ms.topic.domain.service.TopicDomainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TopicApplicationService implements TopicUseCase {

    private static final String MESSAGE_TOPIC = "topic";

    private final TopicDomainService domainTopicService;

    public Optional<TopicDTO> getTopicById(Long id) throws ResourceNotFoundException {
        log.debug("Fetching topic with id {}", id);

        TopicModel topicModel = domainTopicService.getTopicById(id).orElseThrow(
                () -> new ResourceNotFoundException(MESSAGE_TOPIC, String.valueOf(id))
        );

        TopicDTO topicDto = TopicApplicationMapper.toDTO(topicModel);

        log.info("Successfully retrieved topic with id {}, name '{}', description '{}'",
                id, topicDto.getName(), topicDto.getDescription());

        return Optional.of(topicDto);
    }

    public List<TopicDTO> getAllTopics() {
        log.info("Fetching all topics from database...");

        return domainTopicService.getAllTopics()
                .parallelStream()
                .map(TopicApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TopicDTO createTopic(TopicDTO topicDto) {
        log.debug("Creating new topic with name '{}', description '{}'", topicDto.getName(), topicDto.getDescription());

        TopicModel topicModel = TopicApplicationMapper.toModel(topicDto);

        Optional<TopicModel> topicExist = domainTopicService.getTopicByName(topicModel.getName());

        if (topicExist.isPresent()) {
            throw new ResourceAlreadyExistException(MESSAGE_TOPIC, "name", topicModel.getName());
        }

        TopicModel topicCreated = domainTopicService.createTopic(topicModel);

        log.info("Successfully created topic with id {}, name '{}', description '{}'",
                topicCreated.getId(), topicCreated.getName(), topicCreated.getDescription());

        return TopicApplicationMapper.toDTO(topicCreated);
    }

    public TopicDTO updateTopic(Long id, TopicDTO topicDto) throws ResourceNotFoundException {
        log.debug("Updating topic with id '{}', name '{}', description '{}'",
                topicDto.getId(), topicDto.getName(), topicDto.getDescription());

        TopicModel topicModel = TopicApplicationMapper.toModel(topicDto);

        domainTopicService.getTopicById(topicModel.getId()).orElseThrow(
                () -> new ResourceNotFoundException(MESSAGE_TOPIC, topicModel.getId().toString())
        );

        Optional<TopicModel> topicUpdated = domainTopicService.updateTopic(id, topicModel);

        log.info("Successfully updated topic with id '{}', name '{}', description '{}'",
                topicUpdated.get().getId(),
                topicUpdated.get().getName(),
                topicUpdated.get().getDescription());

        return TopicApplicationMapper.toDTO(topicUpdated.get());
    }

    public void deleteTopicById(Long id) throws ResourceNotFoundException {
        log.debug("Deleting topic with id {}", id);

        TopicModel topicToDelete = domainTopicService.getTopicById(id).orElseThrow(
                () -> new ResourceNotFoundException(MESSAGE_TOPIC, String.valueOf(id))
        );

        domainTopicService.deleteTopic(id);

        log.info("Successfully deleted topic with id '{}', name '{}', description '{}'",
                topicToDelete.getId(), topicToDelete.getName(), topicToDelete.getDescription());
    }

}