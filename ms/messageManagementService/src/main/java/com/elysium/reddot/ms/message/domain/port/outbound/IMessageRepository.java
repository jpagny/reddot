package com.elysium.reddot.ms.message.domain.port.outbound;

import com.elysium.reddot.ms.message.domain.model.MessageModel;

import java.util.List;
import java.util.Optional;

public interface IMessageRepository {

    MessageModel createMessage(MessageModel messageModel);

    Optional<MessageModel> findMessageById(Long id);

    Optional<MessageModel> findFirstByContentAndThreadId(String content, Long parentMessageId);

    List<MessageModel> findAllMessages();

    MessageModel updateMessage(MessageModel messageModel);

}
