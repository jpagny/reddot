package com.elysium.reddot.ms.replymessage.domain.port.inbound;

import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;

import java.util.List;

public interface IReplyMessageManagementService {

    ReplyMessageModel getReplyMessageById(Long id);

    List<ReplyMessageModel> getAllRepliesMessage();

    ReplyMessageModel createReplyMessage(ReplyMessageModel replyMessageModel);

    //MessageModel updateMessage(Long id, MessageModel messageModel);


}
