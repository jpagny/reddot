package com.elysium.reddot.ms.message.application.service;

import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.outbound.persistence.MessageRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * This service is responsible for interacting with RabbitMQ and perform operations related to Messages.
 */
@Service
@RequiredArgsConstructor
public class MessageRabbitMQService {

    private final MessageRepositoryAdapter messageRepository;

    /**
     * Checks if a Message exists in the database with the given id.
     *
     * @param id - Id of the Message.
     * @return - true if the Message exists, false otherwise.
     */
    public boolean checkMessageIdExists(Long id) {
        Optional<MessageModel> messageModel = messageRepository.findMessageById(id);
        return messageModel.isPresent();
    }

    /**
     * Counts the number of Messages made by a user between two dates.
     *
     * @param userId - User's id.
     * @param onStart - The start of the date range.
     * @param onEnd - The end of the date range.
     * @return - The number of Messages made by the user between onStart and onEnd dates.
     */
    public Integer countMessageByUserIdBetweenTwoDates(String userId, LocalDateTime onStart, LocalDateTime onEnd) {
        List<MessageModel> listMessages = messageRepository.listMessagesByUserAndRangeDate(userId, onStart, onEnd);
        return listMessages.size();
    }


}
