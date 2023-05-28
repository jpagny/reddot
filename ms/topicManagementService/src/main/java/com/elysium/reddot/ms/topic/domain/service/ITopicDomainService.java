package com.elysium.reddot.ms.topic.domain.service;

import com.elysium.reddot.ms.topic.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.topic.domain.exception.type.FieldWithSpaceException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;

/**
 * This interface defines the contract for business-level operations and validations
 * related to TopicModels.
 */
public interface ITopicDomainService {

    /**
     * Validates a TopicModel for creation.
     *
     * @param topic The TopicModel to be validated for creation.
     * @throws FieldEmptyException     If any required field in the TopicModel is empty.
     * @throws FieldWithSpaceException If any field in the TopicModel contains spaces.
     */
    void validateTopicForCreation(TopicModel topic);

    /**
     * Validates a TopicModel for update.
     *
     * @param topic The TopicModel to be validated for update.
     * @throws FieldEmptyException     If any required field in the TopicModel is empty.
     * @throws FieldWithSpaceException If any field in the TopicModel contains spaces.
     */
    void validateTopicForUpdate(TopicModel topic);

    /**
     * Updates an existing TopicModel with new data from a provided TopicModel.
     *
     * @param existingTopic The TopicModel to be updated.
     * @param topicToUpdate The TopicModel containing the new data.
     * @return The updated TopicModel.
     */
    TopicModel updateExistingTopicWithUpdates(TopicModel existingTopic, TopicModel topicToUpdate);

}
