package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class ReplyMessageProcessorHolder {
    private final GetAllRepliesMessageProcessor getAllRepliesMessageProcessor;
    private final GetReplyMessageByIdProcessor getReplyMessageByIdProcessor;
    private final CreateReplyMessageProcessor createReplyMessageProcessor;
    private final UpdateReplyMessageProcessor updateReplyMessageProcessor;
}