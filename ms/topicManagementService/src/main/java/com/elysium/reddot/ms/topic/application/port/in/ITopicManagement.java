package com.elysium.reddot.ms.topic.application.port.in;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;

public interface ITopicManagement {

    void validateTopicForCreation(TopicModel topic);

    void validateTopicForUpdate(TopicModel topic);

    TopicModel updateExistingTopicWithUpdates(TopicModel existingTopic, TopicModel topicToUpdate);

}
