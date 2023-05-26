package com.elysium.reddot.ms.replymessage.domain.service;

public interface IReplyMessageDomainService {

    void verifyNestedRepliesLimit(int count, int maxNestedReplies);


}
