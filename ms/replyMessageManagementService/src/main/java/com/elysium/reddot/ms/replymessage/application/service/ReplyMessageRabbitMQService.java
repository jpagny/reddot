package com.elysium.reddot.ms.replymessage.application.service;

import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.ReplyMessageRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyMessageRabbitMQService {

    private final ReplyMessageRepositoryAdapter replyMessageRepository;


    public Integer countRepliesMessageByUserIdBetweenTwoDates(String userId, LocalDateTime onStart, LocalDateTime onEnd){
        List<ReplyMessageModel> listRepliesMessage = replyMessageRepository.listMessagesByUserAndRangeDate(userId,onStart,onEnd);
        return listRepliesMessage.size();
    }


}
