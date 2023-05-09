package com.elysium.reddot.ms.replymessage.infrastructure.mapper;


import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.entity.ReplyMessageJpaEntity;

public class ReplyMessagePersistenceMapper {

    private ReplyMessagePersistenceMapper() {
    }

    public static ReplyMessageJpaEntity toEntity(ReplyMessageModel replyReplyMessageModel) {
        ReplyMessageJpaEntity replyReplyMessageJpaEntity = new ReplyMessageJpaEntity();
        replyReplyMessageJpaEntity.setId(replyReplyMessageModel.getId());
        replyReplyMessageJpaEntity.setContent(replyReplyMessageModel.getContent());
        replyReplyMessageJpaEntity.setParentMessageId(replyReplyMessageModel.getParentMessageID());
        replyReplyMessageJpaEntity.setUserId(replyReplyMessageModel.getUserId());
        return replyReplyMessageJpaEntity;
    }

    public static ReplyMessageModel toModel(ReplyMessageJpaEntity replyReplyMessageJpaEntity) {
        return new ReplyMessageModel(
                replyReplyMessageJpaEntity.getId(),
                replyReplyMessageJpaEntity.getContent(),
                replyReplyMessageJpaEntity.getParentMessageId(),
                replyReplyMessageJpaEntity.getUserId()
        );
    }

}
