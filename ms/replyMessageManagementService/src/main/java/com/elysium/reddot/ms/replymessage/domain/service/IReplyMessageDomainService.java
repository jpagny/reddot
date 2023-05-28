package com.elysium.reddot.ms.replymessage.domain.service;

import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;

public interface IReplyMessageDomainService {

    void validateReplyMessageForCreation(ReplyMessageModel replyMessageModel);

    void validateReplyMessageForUpdate(ReplyMessageModel replyMessageModel, ReplyMessageModel replyMessageExisting);
    
    void verifyNestedRepliesLimit(int count, int maxNestedReplies);


}
