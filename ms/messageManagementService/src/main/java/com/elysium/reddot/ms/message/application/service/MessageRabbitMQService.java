package com.elysium.reddot.ms.message.application.service;

import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.domain.port.outbound.IMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageRabbitMQService {

    private final IMessageRepository boardRepository;

    public boolean checkMessageIdExists(Long id) {
        Optional<MessageModel> topicModel = boardRepository.findMessageById(id);
        return topicModel.isPresent();
    }


}
