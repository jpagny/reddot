package com.elysium.reddot.ms.replymessage.application.service;

import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.ReplyMessageRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service to handle operations related to reply messages via RabbitMQ.
 */
@Service
@RequiredArgsConstructor
public class ReplyMessageRabbitMQService {

    private final ReplyMessageRepositoryAdapter replyMessageRepository;

    /**
     * Counts the number of reply messages created by a specific user between two dates.
     *
     * @param userId the unique identifier of the user
     * @param onStart the start date and time
     * @param onEnd the end date and time
     * @return the number of reply messages created by the user between the start and end dates
     */
    public Integer countRepliesMessageByUserIdBetweenTwoDates(String userId, LocalDateTime onStart, LocalDateTime onEnd){
        List<ReplyMessageModel> listRepliesMessage = replyMessageRepository.listMessagesByUserAndRangeDate(userId,onStart,onEnd);
        return listRepliesMessage.size();
    }


}
