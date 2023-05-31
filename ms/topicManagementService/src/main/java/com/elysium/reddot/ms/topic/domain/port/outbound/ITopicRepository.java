package com.elysium.reddot.ms.topic.domain.port.outbound;

import com.elysium.reddot.ms.topic.domain.model.TopicModel;

import java.util.List;
import java.util.Optional;

/**
 * This interface defines the contract for topic repository operations.
 */
public interface ITopicRepository {

    /**
     * Creates a new topic.
     *
     * @param topicModel The TopicModel to be created.
     * @return The created TopicModel.
     */
    TopicModel createTopic(TopicModel topicModel);

    /**
     * Finds a TopicModel by its unique identifier.
     *
     * @param id The unique identifier of the topic.
     * @return An Optional containing the TopicModel if it exists, empty otherwise.
     */
    Optional<TopicModel> findTopicById(Long id);

    /**
     * Finds a TopicModel by its name.
     *
     * @param name The name of the topic.
     * @return An Optional containing the TopicModel if it exists, empty otherwise.
     */
    Optional<TopicModel> findTopicByName(String name);

    /**
     * Finds all TopicModels.
     *
     * @return A list containing all TopicModels.
     */
    List<TopicModel> findAllTopics();

    /**
     * Updates an existing topic.
     *
     * @param topicModel The TopicModel to be updated.
     * @return The updated TopicModel.
     */
    TopicModel updateTopic(TopicModel topicModel);

}
