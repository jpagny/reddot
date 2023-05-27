package com.elysium.reddot.ms.message.infrastructure.outbound.persistence;


import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.domain.port.outbound.IMessageRepository;
import com.elysium.reddot.ms.message.infrastructure.mapper.MessagePersistenceMapper;
import com.elysium.reddot.ms.message.infrastructure.outbound.persistence.entity.MessageJpaEntity;
import com.elysium.reddot.ms.message.infrastructure.outbound.persistence.repository.MessageJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MessageRepositoryAdapter implements IMessageRepository {

    private final MessageJpaRepository messageJpaRepository;

    public MessageRepositoryAdapter(MessageJpaRepository messageJpaRepository) {
        this.messageJpaRepository = messageJpaRepository;
    }

    @Override
    public MessageModel createMessage(MessageModel messageModel) {
        MessageJpaEntity messageEntity = MessagePersistenceMapper.toEntity(messageModel);
        MessageJpaEntity savedMessage = messageJpaRepository.save(messageEntity);
        return MessagePersistenceMapper.toModel(savedMessage);
    }

    @Override
    public Optional<MessageModel> findMessageById(Long id) {
        return messageJpaRepository.findById(id)
                .map(MessagePersistenceMapper::toModel);
    }

    @Override
    public Optional<MessageModel> findFirstByContentAndThreadId(String content, Long threadId) {
        return messageJpaRepository.findFirstByContentAndThreadId(content,threadId)
                .map(MessagePersistenceMapper::toModel);
    }

    @Override
    public List<MessageModel> findAllMessages() {
        return messageJpaRepository.findAll()
                .stream()
                .map(MessagePersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public MessageModel updateMessage(MessageModel messageModel) {
        MessageJpaEntity messageEntity = MessagePersistenceMapper.toEntity(messageModel);
        MessageJpaEntity updatedMessage = messageJpaRepository.save(messageEntity);
        return MessagePersistenceMapper.toModel(updatedMessage);
    }

    public List<MessageModel> listMessagesByUserAndRangeDate(String userId, LocalDateTime onStart, LocalDateTime onEnd) {
        return messageJpaRepository.findMessagesByUserIdAndDateRange(userId, onStart, onEnd)
                .stream()
                .map(MessagePersistenceMapper::toModel)
                .collect(Collectors.toList());
    }
}
