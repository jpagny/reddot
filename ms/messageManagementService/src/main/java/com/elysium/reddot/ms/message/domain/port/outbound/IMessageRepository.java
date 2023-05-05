package com.elysium.reddot.ms.message.domain.port.outbound;

import com.elysium.reddot.ms.message.domain.model.MessageModel;

import java.util.List;
import java.util.Optional;

public interface IMessageRepository {

    MessageModel createMessage(MessageModel messageModel);

    Optional<MessageModel> findMessageById(Long id);

    List<MessageModel> findAllMessages();

    MessageModel updateMessage(MessageModel messageModel);

    void deleteMessage(Long id);
}
