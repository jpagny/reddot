package com.elysium.reddot.ms.replymessage.infrastructure.mapper;


import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.entity.ReplyMessageJpaEntity;

public class ReplyMessageJpaReplyMessageModelMapper {

    private ReplyMessageJpaReplyMessageModelMapper() {
    }


    /**
     * Maps a ReplyMessageModel to a ReplyMessageJpaEntity.
     *
     * @param replyMessageModel the ReplyMessageModel to be mapped
     * @return the mapped ReplyMessageJpaEntity
     */
    public static ReplyMessageJpaEntity toEntity(ReplyMessageModel replyMessageModel) {
        ReplyMessageJpaEntity replyReplyMessageJpaEntity = new ReplyMessageJpaEntity();
        replyReplyMessageJpaEntity.setId(replyMessageModel.getId());
        replyReplyMessageJpaEntity.setContent(replyMessageModel.getContent());
        replyReplyMessageJpaEntity.setParentMessageId(replyMessageModel.getParentMessageID());
        replyReplyMessageJpaEntity.setUserId(replyMessageModel.getUserId());
        replyReplyMessageJpaEntity.setCreatedAt(replyMessageModel.getCreatedAt());
        replyReplyMessageJpaEntity.setUpdatedAt(replyMessageModel.getUpdatedAt());
        return replyReplyMessageJpaEntity;
    }

    /**
     * Maps a ReplyMessageJpaEntity to a ReplyMessageModel.
     *
     * @param replyMessageJpaEntity the ReplyMessageJpaEntity to be mapped
     * @return the mapped ReplyMessageModel
     */
    public static ReplyMessageModel toModel(ReplyMessageJpaEntity replyMessageJpaEntity) {
        return new ReplyMessageModel(
                replyMessageJpaEntity.getId(),
                replyMessageJpaEntity.getContent(),
                replyMessageJpaEntity.getParentMessageId(),
                replyMessageJpaEntity.getUserId(),
                replyMessageJpaEntity.getCreatedAt(),
                replyMessageJpaEntity.getUpdatedAt()
        );
    }

}
