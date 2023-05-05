package com.elysium.reddot.ms.message.domain.service;

import com.elysium.reddot.ms.message.domain.model.MessageModel;

public interface IMessageDomainService {

    void validateMessageForCreation(MessageModel message);

    void validateMessageForUpdate(MessageModel message);

    MessageModel updateExistingMessageWithUpdates(MessageModel existingMessage, MessageModel messageToUpdate);

}
