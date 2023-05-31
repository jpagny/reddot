package com.elysium.reddot.ms.message.domain.port.outbound;

import com.elysium.reddot.ms.message.domain.model.MessageModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * This interface provides a contract for a repository handling operations on the {@link MessageModel} class.
 */
public interface IMessageRepository {

    /**
     * Creates a new {@link MessageModel} instance in the system.
     *
     * @param messageModel the {@link MessageModel} instance to create
     * @return the created {@link MessageModel} instance
     */
    MessageModel createMessage(MessageModel messageModel);

    /**
     * Retrieves a specific {@link MessageModel} by its unique identifier.
     *
     * @param id the unique identifier of the message to retrieve
     * @return an Optional containing the {@link MessageModel} with the specified id, or empty if no such message exists
     */
    Optional<MessageModel> findMessageById(Long id);

    /**
     * Retrieves the first {@link MessageModel} with the specified content and thread ID.
     *
     * @param content the content of the message to retrieve
     * @param parentMessageId the ID of the thread the message belongs to
     * @return an Optional containing the first {@link MessageModel} matching the specified content and thread ID, or empty if no such message exists
     */
    Optional<MessageModel> findFirstByContentAndThreadId(String content, Long parentMessageId);

    /**
     * Retrieves all {@link MessageModel} instances stored in the system.
     *
     * @return a list of all {@link MessageModel} instances
     */
    List<MessageModel> findAllMessages();

    /**
     * Updates a specific {@link MessageModel} instance.
     *
     * @param messageModel the {@link MessageModel} instance containing the new data
     * @return the updated {@link MessageModel} instance
     */
    MessageModel updateMessage(MessageModel messageModel);

    /**
     * Retrieves a list of messages created by the specified user within the specified date range.
     *
     * @param userId  the ID of the user
     * @param onStart the start date of the range
     * @param onEnd   the end date of the range
     * @return a list of {@link MessageModel} instances created by the user within the specified date range
     */
    List<MessageModel> listMessagesByUserAndRangeDate(String userId, LocalDateTime onStart, LocalDateTime onEnd);

}
