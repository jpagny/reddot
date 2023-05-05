package com.elysium.reddot.ms.message.infrastructure.mapper;

import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.outbound.persistence.entity.MessageJpaEntity;

public class MessagePersistenceMapper {

    private MessagePersistenceMapper() {
    }

    public static MessageJpaEntity toEntity(MessageModel messageModel) {
        MessageJpaEntity messageJpaEntity = new MessageJpaEntity();
        messageJpaEntity.setId(messageModel.getId());
        messageJpaEntity.setContent(messageModel.getContent());
        messageJpaEntity.setThreadId(messageModel.getThreadId());
        messageJpaEntity.setUserId(messageModel.getUserId());
        messageJpaEntity.setMessageId(messageModel.getMessageId());
        return messageJpaEntity;
    }

    public static MessageModel toModel(MessageJpaEntity messageJpaEntity) {
        return new MessageModel(
                messageJpaEntity.getId(),
                messageJpaEntity.getContent(),
                messageJpaEntity.getThreadId(),
                messageJpaEntity.getUserId(),
                messageJpaEntity.getMessageId()
        );
    }

}
