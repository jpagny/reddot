package com.elysium.reddot.ms.topic.domain.service;

import com.elysium.reddot.ms.topic.application.port.in.TopicManagement;
import com.elysium.reddot.ms.topic.domain.exception.LabelEmptyException;
import com.elysium.reddot.ms.topic.domain.exception.NameEmptyException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

public class TopicDomainService implements TopicManagement {

    @Override
    public void validateBuildTopic(TopicModel topicModel) {
        validateName(topicModel.getName());
        validateLabel(topicModel.getLabel());
    }

    @Override
    public void validateUpdateTopic(TopicModel topicModel) {
        validateLabel(topicModel.getLabel());
    }

    @Override
    public TopicModel updateTopic(TopicModel existingTopic, TopicModel topicToUpdate) {

        validateUpdateTopic(topicToUpdate);

        existingTopic.setLabel(topicToUpdate.getLabel());
        existingTopic.setDescription(topicToUpdate.getDescription());

        return existingTopic.clone();
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            throw new NameEmptyException();
        }
    }

    private void validateLabel(String label) {
        if (isBlank(label)) {
            throw new LabelEmptyException();
        }
    }

}
