package com.elysium.reddot.ms.message.application.service;

import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.domain.port.outbound.IMessageRepository;
import com.elysium.reddot.ms.message.infrastructure.outbound.persistence.MessageRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageRabbitMQService {

    private final MessageRepositoryAdapter messageRepository;

    public boolean checkMessageIdExists(Long id) {
        Optional<MessageModel> topicModel = messageRepository.findMessageById(id);
        return topicModel.isPresent();
    }

    public Integer countMessageByUserIdBetweenTwoDates(String userId, LocalDateTime onStart, LocalDateTime onEnd){
        List<MessageModel> listMessages = messageRepository.listMessagesByUserAndRangeDate(userId,onStart,onEnd);
        return listMessages.size();
    }


}
