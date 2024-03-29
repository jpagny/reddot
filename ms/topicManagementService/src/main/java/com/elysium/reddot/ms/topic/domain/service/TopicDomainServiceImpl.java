package com.elysium.reddot.ms.topic.domain.service;

import com.elysium.reddot.ms.topic.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.topic.domain.exception.type.FieldWithSpaceException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

/**
 * This class provides an implementation of the ITopicDomainService interface.
 * It provides methods for validating and updating TopicModels.
 */
public class TopicDomainServiceImpl implements ITopicDomainService {

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateTopicForCreation(TopicModel topicModel) {
        validateName(topicModel.getName());
        validateLabel(topicModel.getLabel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateTopicForUpdate(TopicModel topicModel) {
        validateLabel(topicModel.getLabel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopicModel updateExistingTopicWithUpdates(TopicModel existingTopic, TopicModel topicUpdates) {
        validateTopicForUpdate(topicUpdates);

        existingTopic.setLabel(topicUpdates.getLabel());
        existingTopic.setDescription(topicUpdates.getDescription());

        return existingTopic;
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            throw new FieldEmptyException("name");
        }
        if (containsSpace(name)) {
            throw new FieldWithSpaceException("name");
        }
    }

    private void validateLabel(String label) {
        if (isBlank(label)) {
            throw new FieldEmptyException("label");
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean containsSpace(String str) {
        return str.contains(" ");
    }
}
