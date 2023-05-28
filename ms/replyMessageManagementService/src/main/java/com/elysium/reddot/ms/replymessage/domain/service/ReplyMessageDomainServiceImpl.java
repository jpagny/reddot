package com.elysium.reddot.ms.replymessage.domain.service;

import com.elysium.reddot.ms.replymessage.domain.exception.DifferentUserException;
import com.elysium.reddot.ms.replymessage.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.replymessage.domain.exception.LimitExceededException;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;

public class ReplyMessageDomainServiceImpl implements IReplyMessageDomainService {

    @Override
    public void validateReplyMessageForCreation(ReplyMessageModel replyMessageModel) {
        validateUserFromCreation(replyMessageModel.getUserId());
        validateContent(replyMessageModel.getContent());
    }

    @Override
    public void verifyNestedRepliesLimit(int countRepliesMessage, int maxNestedReplies) {
        if (countRepliesMessage >= maxNestedReplies) {
            throw new LimitExceededException("Max limit of nested replies reached: " + maxNestedReplies);
        }
    }

    @Override
    public void validateReplyMessageForUpdate(ReplyMessageModel replyMessageModel, ReplyMessageModel replyMessageExisting) {
        validateUserFromUpdate(replyMessageModel.getUserId(), replyMessageExisting.getUserId());
        validateContent(replyMessageModel.getContent());
    }

    private void validateUserFromCreation(String userFromMessageToCreate) {
        if (isBlank(userFromMessageToCreate)) {
            throw new FieldEmptyException("userId");
        }
    }

    private void validateUserFromUpdate(String userFromMessageToCreate, String userFromMessageExisting) {
        if (isBlank(userFromMessageToCreate)) {
            throw new FieldEmptyException("userId");
        }

        if (isBlank(userFromMessageExisting)) {
            throw new FieldEmptyException("userId");
        }

        if (!userFromMessageToCreate.equals(userFromMessageExisting)) {
            throw new DifferentUserException(userFromMessageToCreate, userFromMessageExisting);
        }
    }

    private void validateContent(String name) {
        if (isBlank(name)) {
            throw new FieldEmptyException("content");
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }


}
