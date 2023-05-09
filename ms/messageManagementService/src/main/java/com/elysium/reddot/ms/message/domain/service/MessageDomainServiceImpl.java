package com.elysium.reddot.ms.message.domain.service;

public class MessageDomainServiceImpl implements IMessageDomainService {

    int MAX_NESTED_REPLIES = 10;

    boolean checkNestedRepliesLimit(int countRepliesMessage) {
        return checkNestedRepliesLimit(countRepliesMessage, MAX_NESTED_REPLIES);
    }

    boolean checkNestedRepliesLimit(int countRepliesMessage, int maxReplies) {
        return countRepliesMessage <= maxReplies;
    }

}
