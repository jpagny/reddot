package com.elysium.reddot.ms.topic.application.port.in;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;

public interface TopicManagement {

    void validateTopic(TopicModel topic);

    TopicModel updateTopic(TopicModel existingTopic, TopicModel topicToUpdate);

}
