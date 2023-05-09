package com.elysium.reddot.ms.message.domain.port.inbound;

import com.elysium.reddot.ms.message.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.message.domain.model.MessageModel;

import java.util.List;

public interface IMessageManagementService {

    MessageModel getMessageById(Long id);

    List<MessageModel> getAllMessages();

    MessageModel createMessage(MessageModel messageModel) throws FieldEmptyException;

    //MessageModel updateMessage(Long id, MessageModel messageModel);

}
