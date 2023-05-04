package com.elysium.reddot.ms.board.domain.port.inbound;

public interface ITopicManagementService {
    boolean topicExists(long topicId);
}
