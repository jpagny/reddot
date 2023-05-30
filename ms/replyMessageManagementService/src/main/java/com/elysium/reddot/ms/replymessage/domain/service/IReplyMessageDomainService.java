package com.elysium.reddot.ms.replymessage.domain.service;

import com.elysium.reddot.ms.replymessage.domain.exception.LimitExceededException;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;

/**
 * Interface for reply message domain operations.
 * This includes validation logic and business rules.
 */
public interface IReplyMessageDomainService {

    /**
     * Validates the specified reply message for creation.
     * This could include checking that all necessary fields are filled in and meet any format requirements.
     *
     * @param replyMessageModel the reply message to validate
     * @throws IllegalArgumentException if the reply message is not valid for creation
     */
    void validateReplyMessageForCreation(ReplyMessageModel replyMessageModel);


    /**
     * Validates the specified reply message for update.
     * This could include checking that all necessary fields are filled in and meet any format requirements,
     * and that the new data is consistent with the existing data.
     *
     * @param replyMessageModel the new data for the reply message
     * @param replyMessageExisting the existing reply message to be updated
     * @throws IllegalArgumentException if the reply message is not valid for update
     */
    void validateReplyMessageForUpdate(ReplyMessageModel replyMessageModel, ReplyMessageModel replyMessageExisting);

    /**
     * Verifies that the number of nested replies does not exceed the specified limit.
     *
     * @param count the current number of nested replies
     * @param maxNestedReplies the maximum number of nested replies allowed
     * @throws LimitExceededException if the number of nested replies exceeds the maximum
     */
    void verifyNestedRepliesLimit(int count, int maxNestedReplies);


}
