package com.elysium.reddot.ms.topic.application.service;

import com.elysium.reddot.ms.topic.application.port.in.TopicUseCase;
import com.elysium.reddot.ms.topic.application.port.out.TopicRepository;
import com.elysium.reddot.ms.topic.application.service.dto.TopicDTO;
import com.elysium.reddot.ms.topic.domain.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.domain.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicUseCase {

    // USE TopicModel and not TopicDTO !!! Use Mapper !

    private static final String MESSAGE_TOPIC = "topic";

    private final TopicRepository topicRepository;

    @Override
    public Optional<TopicDTO> getTopicById(Long id) throws ResourceNotFoundException {
        log.debug("Fetching topic with id {}", id);

        TopicDTO topicDTO = topicRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(MESSAGE_TOPIC, String.valueOf(id))
        );

        log.info("Successfully retrieved topic with id {}, name '{}', description '{}'",
                id, topicDTO.getName(), topicDTO.getDescription());

        return Optional.of(topicDTO);
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        log.info("Fetching all topics from database...");

        return topicRepository.findAll()
                .parallelStream()
                .collect(Collectors.toList());
    }

    @Override
    public TopicDTO createTopic(TopicDTO topicDTO) {
        log.debug("Creating new topic with name '{}', description '{}'", topicDTO.getName(), topicDTO.getDescription());

        Optional<TopicDTO> topicExist = topicRepository.findByName(topicDTO.getName());

        if (topicExist.isPresent()) {
            throw new ResourceAlreadyExistException(MESSAGE_TOPIC, "name", topicDTO.getName());
        }

        TopicDTO topicCreated = topicRepository.save(topicDTO);

        log.info("Successfully created topic with id {}, name '{}', description '{}'",
                topicCreated.getId(), topicCreated.getName(), topicCreated.getDescription());

        return topicCreated;
    }

    @Override
    public TopicDTO updateTopic(Long id, TopicDTO topicDTO) throws ResourceNotFoundException {
        log.debug("Updating topic with id '{}', name '{}', description '{}'",
                topicDTO.getId(), topicDTO.getName(), topicDTO.getDescription());

        topicRepository.findById(topicDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException(MESSAGE_TOPIC, topicDTO.getId().toString())
        );


        TopicDTO topicUpdated = topicRepository.save(topicDTO);

        log.info("Successfully updated topic with id '{}', name '{}', description '{}'",
                topicUpdated.getId(),
                topicUpdated.getName(),
                topicUpdated.getDescription());

        return topicUpdated;
    }

    @Override
    public void deleteTopicById(Long id) throws ResourceNotFoundException {
        log.debug("Deleting topic with id {}", id);

        TopicDTO topicToDelete = topicRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(MESSAGE_TOPIC, String.valueOf(id))
        );

        topicRepository.deleteById(id);

        log.info("Successfully deleted topic with id '{}', name '{}', description '{}'",
                topicToDelete.getId(), topicToDelete.getName(), topicToDelete.getDescription());
    }

}