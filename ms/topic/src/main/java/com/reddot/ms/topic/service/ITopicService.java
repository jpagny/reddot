package com.reddot.ms.topic.service;

import com.reddot.ms.topic.data.dto.TopicDTO;
import com.reddot.ms.topic.exception.ResourceAlreadyExistException;
import com.reddot.ms.topic.exception.ResourceNotFoundException;

import java.util.List;

public interface ITopicService {

    TopicDTO getById(Long id) throws ResourceNotFoundException;

    List<TopicDTO> getAll();

    TopicDTO create(TopicDTO topic) throws ResourceAlreadyExistException;

    TopicDTO update(Long id, TopicDTO topic) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;


}
