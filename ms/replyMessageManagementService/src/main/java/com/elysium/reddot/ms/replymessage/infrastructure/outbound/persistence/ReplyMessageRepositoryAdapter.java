package com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence;


import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.domain.port.outbound.IReplyMessageRepository;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageJpaReplyMessageModelMapper;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.entity.ReplyMessageJpaEntity;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.repository.ReplyMessageJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReplyMessageRepositoryAdapter implements IReplyMessageRepository {

    private final ReplyMessageJpaRepository replyMessageJpaRepository;

    public ReplyMessageRepositoryAdapter(ReplyMessageJpaRepository replyMessageJpaRepository) {
        this.replyMessageJpaRepository = replyMessageJpaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReplyMessageModel createReplyMessage(ReplyMessageModel replyMessageModel) {
        ReplyMessageJpaEntity replyMessageEntity = ReplyMessageJpaReplyMessageModelMapper.toEntity(replyMessageModel);
        ReplyMessageJpaEntity savedReplyMessage = replyMessageJpaRepository.save(replyMessageEntity);
        return ReplyMessageJpaReplyMessageModelMapper.toModel(savedReplyMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ReplyMessageModel> findReplyMessageById(Long id) {
        return replyMessageJpaRepository.findById(id)
                .map(ReplyMessageJpaReplyMessageModelMapper::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReplyMessageModel> findAllRepliesMessage() {
        return replyMessageJpaRepository.findAll()
                .stream()
                .map(ReplyMessageJpaReplyMessageModelMapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ReplyMessageModel> findFirstByContentAndParentMessageId(String content, Long parentMessageId) {
        return replyMessageJpaRepository.findFirstByContentAndThreadId(content,parentMessageId)
                .map(ReplyMessageJpaReplyMessageModelMapper::toModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReplyMessageModel updateReplyMessage(ReplyMessageModel replyMessageModel) {
        ReplyMessageJpaEntity replyMessageEntity = ReplyMessageJpaReplyMessageModelMapper.toEntity(replyMessageModel);
        ReplyMessageJpaEntity updatedReplyMessage = replyMessageJpaRepository.save(replyMessageEntity);
        return ReplyMessageJpaReplyMessageModelMapper.toModel(updatedReplyMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countRepliesByMessageId(Long messageId) {
        return replyMessageJpaRepository.countRepliesByMessageId(messageId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReplyMessageModel> listMessagesByUserAndRangeDate(String userId, LocalDateTime onStart, LocalDateTime onEnd) {
        return replyMessageJpaRepository.findRepliesMessageByUserIdAndDateRange(userId, onStart, onEnd)
                .stream()
                .map(ReplyMessageJpaReplyMessageModelMapper::toModel)
                .collect(Collectors.toList());
    }

}
