package com.elysium.reddot.ms.replymessage.domain.port.outbound;

import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface for managing reply message persistence.
 */
public interface IReplyMessageRepository {

    /**
     * Creates a new reply message in the repository.
     *
     * @param replyMessageModel the reply message to create
     * @return the newly created reply message
     */
    ReplyMessageModel createReplyMessage(ReplyMessageModel replyMessageModel);

    /**
     * Finds a reply message by its ID.
     *
     * @param id the id of the reply message to find
     * @return an Optional containing the found reply message, or an empty Optional if no message was found
     */
    Optional<ReplyMessageModel> findReplyMessageById(Long id);

    /**
     * Finds the first reply message that has the specified content and parent message id.
     *
     * @param content the content of the reply message to find
     * @param parentMessageId the parent message id of the reply message to find
     * @return an Optional containing the found reply message, or an empty Optional if no message was found
     */
    Optional<ReplyMessageModel> findFirstByContentAndParentMessageId(String content, Long parentMessageId);

    /**
     * Returns all reply messages in the repository.
     *
     * @return a list of all reply messages
     */
    List<ReplyMessageModel> findAllRepliesMessage();

    /**
     * Updates a reply message in the repository.
     *
     * @param replyMessageModel the reply message to update
     * @return the updated reply message
     */
    ReplyMessageModel updateReplyMessage(ReplyMessageModel replyMessageModel);

    /**
     * Counts the number of replies to a message.
     *
     * @param messageId the id of the message to count replies for
     * @return the number of replies to the message
     */
    int countRepliesByMessageId(Long messageId);

    /**
     * Returns a list of messages by a user that were created within a specified date range.
     *
     * @param userId the id of the user who created the messages
     * @param onStart the start of the date range
     * @param onEnd the end of the date range
     * @return a list of messages by the user within the date range
     */
    List<ReplyMessageModel> listMessagesByUserAndRangeDate(String userId, LocalDateTime onStart, LocalDateTime onEnd);


}
