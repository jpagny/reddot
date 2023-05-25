package com.elysium.reddot.ms.message.domain.service;

import com.elysium.reddot.ms.message.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.message.domain.model.MessageModel;

public class MessageDomainServiceImpl implements IMessageDomainService {


    @Override
    public void validateTopicForCreation(MessageModel messageModel) {
        validateName(messageModel.getContent());
    }

    @Override
    public void validateTopicForUpdate(MessageModel messageModel) {
        validateName(messageModel.getContent());
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            throw new FieldEmptyException("content");
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }


}
