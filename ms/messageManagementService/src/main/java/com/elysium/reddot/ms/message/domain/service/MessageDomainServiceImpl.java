package com.elysium.reddot.ms.message.domain.service;

import com.elysium.reddot.ms.message.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.message.domain.exception.FieldWithSpaceException;
import com.elysium.reddot.ms.message.domain.model.MessageModel;

public class MessageDomainServiceImpl implements IMessageDomainService {

    @Override
    public void validateMessageForCreation(MessageModel messageModel) {
        validateLabel(messageModel.getContent());
    }

    @Override
    public void validateMessageForUpdate(MessageModel messageModel) {
        validateLabel(messageModel.getContent());
    }

    @Override
    public MessageModel updateExistingMessageWithUpdates(MessageModel existingMessage, MessageModel messageUpdates) {
        validateMessageForUpdate(messageUpdates);

        existingMessage.setContent(messageUpdates.getContent());

        return existingMessage;
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            throw new FieldEmptyException("name");
        }
        if (containsSpace(name)) {
            throw new FieldWithSpaceException("name");
        }
    }

    private void validateLabel(String label) {
        if (isBlank(label)) {
            throw new FieldEmptyException("label");
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean containsSpace(String str) {
        return str.contains(" ");
    }
}
