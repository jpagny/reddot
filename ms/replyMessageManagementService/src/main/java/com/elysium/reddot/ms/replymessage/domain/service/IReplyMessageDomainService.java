package com.elysium.reddot.ms.replymessage.domain.service;

import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;

public interface IReplyMessageDomainService {

    void verifyNestedRepliesLimit(int count, int maxNestedReplies);



}
