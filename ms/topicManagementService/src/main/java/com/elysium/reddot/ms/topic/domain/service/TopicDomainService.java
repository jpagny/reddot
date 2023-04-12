package com.elysium.reddot.ms.topic.domain.service;

import com.elysium.reddot.ms.topic.application.port.in.TopicManagement;
import com.elysium.reddot.ms.topic.domain.exception.LabelEmptyException;
import com.elysium.reddot.ms.topic.domain.exception.NameEmptyException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import org.springframework.stereotype.Component;

public class TopicDomainService implements TopicManagement {

    @Override
    public void validateTopic(TopicModel topicModel) {

        if (topicModel.getName() == null
                || topicModel.getName().isEmpty()
                || topicModel.getName().isBlank()) {
            throw new NameEmptyException();
        }

        if (topicModel.getLabel() == null
                || topicModel.getLabel().isEmpty()
                || topicModel.getLabel().isBlank()) {
            throw new LabelEmptyException();
        }
    }

    @Override
    public TopicModel updateTopicProperties(TopicModel existingTopic, TopicModel topicToUpdate) {
        existingTopic.setLabel(topicToUpdate.getLabel());
        existingTopic.setDescription(topicToUpdate.getDescription());
        return existingTopic;
    }
}
