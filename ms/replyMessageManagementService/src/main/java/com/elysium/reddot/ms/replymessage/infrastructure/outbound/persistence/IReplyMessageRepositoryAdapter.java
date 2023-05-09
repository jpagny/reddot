package com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence;


import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.domain.port.outbound.IReplyMessageRepository;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessagePersistenceMapper;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.entity.ReplyMessageJpaEntity;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.repository.ReplyMessageJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class IReplyMessageRepositoryAdapter implements IReplyMessageRepository {

    private final ReplyMessageJpaRepository replyMessageJpaRepository;

    public IReplyMessageRepositoryAdapter(ReplyMessageJpaRepository replyMessageJpaRepository) {
        this.replyMessageJpaRepository = replyMessageJpaRepository;
    }

    @Override
    public ReplyMessageModel createReplyMessage(ReplyMessageModel replyMessageModel) {
        ReplyMessageJpaEntity replyMessageEntity = ReplyMessagePersistenceMapper.toEntity(replyMessageModel);
        ReplyMessageJpaEntity savedReplyMessage = replyMessageJpaRepository.save(replyMessageEntity);
        return ReplyMessagePersistenceMapper.toModel(savedReplyMessage);
    }

    @Override
    public Optional<ReplyMessageModel> findReplyMessageById(Long id) {
        return replyMessageJpaRepository.findById(id)
                .map(ReplyMessagePersistenceMapper::toModel);
    }

    @Override
    public List<ReplyMessageModel> findAllRepliesMessage() {
        return replyMessageJpaRepository.findAll()
                .stream()
                .map(ReplyMessagePersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public ReplyMessageModel updateReplyMessage(ReplyMessageModel replyMessageModel) {
        ReplyMessageJpaEntity replyMessageEntity = ReplyMessagePersistenceMapper.toEntity(replyMessageModel);
        ReplyMessageJpaEntity updatedReplyMessage = replyMessageJpaRepository.save(replyMessageEntity);
        return ReplyMessagePersistenceMapper.toModel(updatedReplyMessage);
    }

    @Override
    public int countRepliesByMessageId(Long messageId) {
        return replyMessageJpaRepository.countRepliesByMessageId(messageId);
    }

}
