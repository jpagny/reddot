package com.elysium.reddot.ms.topic.domain.service;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;

public interface ITopicDomainService {

    void validateTopicForCreation(TopicModel topic);

    void validateTopicForUpdate(TopicModel topic);

    TopicModel updateExistingTopicWithUpdates(TopicModel existingTopic, TopicModel topicToUpdate);

}
