package com.elysium.reddot.ms.message.infrastructure.inbound.rest.processor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class MessageProcessorHolder {
    private final GetAllMessagesProcessor getAllMessagesProcessor;
    private final GetMessageByIdProcessor getMessageByIdProcessor;
    private final CreateMessageProcessor createMessageProcessor;
    private final UpdateMessageProcessor updateMessageProcessor;
}