package com.reddot.ms.topic.service.implement;

import com.reddot.ms.topic.data.dto.TopicDTO;
import com.reddot.ms.topic.data.entity.TopicEntity;
import com.reddot.ms.topic.data.mapper.TopicMapper;
import com.reddot.ms.topic.data.repository.TopicRepository;
import com.reddot.ms.topic.exception.ResourceAlreadyExistException;
import com.reddot.ms.topic.exception.ResourceNotFoundException;
import com.reddot.ms.topic.service.ITopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("topicService")
@Transactional
@AllArgsConstructor
@Slf4j
public class TopicServiceImpl implements ITopicService {

    private final TopicMapper topicMapper;
    private final TopicRepository topicRepository;

    @Override
    public TopicDTO getById(Long id) throws ResourceNotFoundException {
        log.debug("Fetching topic with id {}", id);

        TopicEntity topicEntity = topicRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("topic", String.valueOf(id))
        );

        log.info("Successfully retrieved topic with id {}, name '{}', description '{}'",
                id, topicEntity.getName(), topicEntity.getDescription());

        return topicMapper.toDTO(topicEntity);
    }

    @Override
    public List<TopicDTO> getAll() {
        log.info("Fetching all topics from database...");

        return topicRepository.findAll()
                .parallelStream()
                .map(topicMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TopicDTO create(TopicDTO topicDTO) {
        log.debug("Creating new topic with name '{}', description '{}'", topicDTO.getName(), topicDTO.getDescription());

        TopicEntity topicToCreate = topicMapper.toEntity(topicDTO);

        Optional<TopicEntity> topicExist = topicRepository.findByName(topicToCreate.getName());

        if (topicExist.isPresent()) {
            throw new ResourceAlreadyExistException("topic", "name", topicToCreate.getName());
        }

        TopicEntity topicCreated = topicRepository.save(topicToCreate);

        log.info("Successfully created topic with id {}, name '{}', description '{}'",
                topicCreated.getId(), topicCreated.getName(), topicCreated.getDescription());

        return topicMapper.toDTO(topicCreated);
    }

    @Override
    public TopicDTO update(Long id, TopicDTO topicDTO) throws ResourceNotFoundException {
        log.debug("Updating topic with id '{}', name '{}', description '{}'",
                topicDTO.getId(), topicDTO.getName(), topicDTO.getDescription());

        topicRepository.findById(topicDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("topic", topicDTO.getId().toString())
        );

        TopicEntity topicToUpdate = topicMapper.toEntity(topicDTO);

        TopicEntity topicUpdated = topicRepository.save(topicToUpdate);

        log.info("Successfully updated topic with id '{}', name '{}', description '{}'",
                topicUpdated.getId(),
                topicUpdated.getName(),
                topicUpdated.getDescription());

        return topicMapper.toDTO(topicUpdated);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        log.debug("Deleting topic with id {}", id);

        TopicEntity topicToDelete = topicRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("topic", String.valueOf(id))
        );

        topicRepository.delete(topicToDelete);

        log.info("Successfully deleted topic with id '{}', name '{}', description '{}'",
                topicToDelete.getId(), topicToDelete.getName(), topicToDelete.getDescription());
    }


}
