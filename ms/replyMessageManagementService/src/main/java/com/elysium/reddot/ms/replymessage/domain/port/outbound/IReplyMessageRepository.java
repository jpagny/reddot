package com.elysium.reddot.ms.replymessage.domain.port.outbound;

import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;

import java.util.List;
import java.util.Optional;

public interface IReplyMessageRepository {

    ReplyMessageModel createReplyMessage(ReplyMessageModel replyMessageModel);

    Optional<ReplyMessageModel> findReplyMessageById(Long id);

    List<ReplyMessageModel> findAllRepliesMessage();

    ReplyMessageModel updateReplyMessage(ReplyMessageModel replyMessageModel);


}
