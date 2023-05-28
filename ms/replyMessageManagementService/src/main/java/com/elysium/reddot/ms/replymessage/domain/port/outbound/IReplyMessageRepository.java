package com.elysium.reddot.ms.replymessage.domain.port.outbound;

import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IReplyMessageRepository {

    ReplyMessageModel createReplyMessage(ReplyMessageModel replyMessageModel);

    Optional<ReplyMessageModel> findReplyMessageById(Long id);

    Optional<ReplyMessageModel> findFirstByContentAndParentMessageId(String content, Long parentMessageId);

    List<ReplyMessageModel> findAllRepliesMessage();

    ReplyMessageModel updateReplyMessage(ReplyMessageModel replyMessageModel);

    int countRepliesByMessageId(Long messageId);

    List<ReplyMessageModel> listMessagesByUserAndRangeDate(String userId, LocalDateTime onStart, LocalDateTime onEnd);


}
