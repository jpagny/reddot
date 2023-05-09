package com.elysium.reddot.ms.replymessage.domain.service;

import com.elysium.reddot.ms.replymessage.domain.exception.LimitExceededException;

public class ReplyMessageDomainServiceImpl implements IReplyMessageDomainService {


    @Override
    public void verifyNestedRepliesLimit(int countRepliesMessage, int maxNestedReplies) {
        if (countRepliesMessage >= maxNestedReplies) {
            throw new LimitExceededException("Max limit of nested replies reached: " + maxNestedReplies);
        }
    }


}
