package com.elysium.reddot.ms.message.domain.service;

import com.elysium.reddot.ms.message.domain.model.MessageModel;

public interface IMessageDomainService {

    void validateTopicForCreation(MessageModel messageModel);

    void validateTopicForUpdate(MessageModel messageModel, MessageModel messageExisting);


}
