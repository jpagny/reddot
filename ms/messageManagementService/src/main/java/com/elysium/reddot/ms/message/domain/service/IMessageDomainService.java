package com.elysium.reddot.ms.message.domain.service;

import com.elysium.reddot.ms.message.domain.model.MessageModel;

public interface IMessageDomainService {

    void validateMessageForCreation(MessageModel messageModel);

    void validateMessageForUpdate(MessageModel messageModel, MessageModel messageExisting);


}
