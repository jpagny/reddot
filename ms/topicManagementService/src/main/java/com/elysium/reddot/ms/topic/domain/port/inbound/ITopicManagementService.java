package com.elysium.reddot.ms.topic.domain.port.inbound;

import com.elysium.reddot.ms.topic.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.topic.domain.model.TopicModel;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * This interface defines the contract for managing topics.
 */
public interface ITopicManagementService {

    /**
     * Gets a TopicModel by its unique identifier.
     *
     * @param id The unique identifier of the topic.
     * @return The TopicModel that matches the provided identifier.
     */
    @ApiOperation(value = "Retrieve a topic by ID", response = TopicModel.class)
    TopicModel getTopicById(Long id);

    /**
     * Gets a list of all TopicModels.
     *
     * @return A list containing all TopicModels.
     */
    @ApiOperation(value = "Retrieve all topics", response = List.class)
    List<TopicModel> getAllTopics();

    /**
     * Creates a new topic.
     *
     * @param topicModel The TopicModel to be created.
     * @return The created TopicModel.
     * @throws FieldEmptyException If any required field in the TopicModel is empty.
     */
    @ApiOperation(value = "Create a new topic", response = TopicModel.class)

    TopicModel createTopic(TopicModel topicModel) throws FieldEmptyException;

    /**
     * Updates an existing topic.
     *
     * @param id         The unique identifier of the topic to be updated.
     * @param topicModel The TopicModel containing the new data.
     * @return The updated TopicModel.
     */
    @ApiOperation(value = "Update a topic by ID", response = TopicModel.class)
    TopicModel updateTopic(Long id, TopicModel topicModel);

}
