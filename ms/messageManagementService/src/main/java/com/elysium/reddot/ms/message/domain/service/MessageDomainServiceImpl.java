package com.elysium.reddot.ms.message.domain.service;

import com.elysium.reddot.ms.message.domain.exception.DifferentUserException;
import com.elysium.reddot.ms.message.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.message.domain.model.MessageModel;

public class MessageDomainServiceImpl implements IMessageDomainService {

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMessageForCreation(MessageModel messageModel) {
        validateContent(messageModel.getContent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMessageForUpdate(MessageModel messageModel, MessageModel messageExisting) {
        validateUser(messageModel.getUserId(), messageExisting.getUserId());
        validateContent(messageModel.getContent());
    }

    private void validateUser(String userFromMessageToCreate, String userFromMessageExisting) {
        if (isBlank(userFromMessageToCreate)) {
            throw new FieldEmptyException("userId from message to create");
        }

        if (isBlank(userFromMessageExisting)) {
            throw new FieldEmptyException("userId from message existing");
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
