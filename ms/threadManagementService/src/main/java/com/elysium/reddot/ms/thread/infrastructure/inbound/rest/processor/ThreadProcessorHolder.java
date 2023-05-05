package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class ThreadProcessorHolder {
    private final GetAllThreadsProcessor getAllThreadsProcessor;
    private final GetThreadByIdProcessor getThreadByIdProcessor;
    private final CreateThreadProcessor createThreadProcessor;
    private final UpdateThreadProcessor updateThreadProcessor;
    private final DeleteThreadProcessor deleteThreadProcessor;

}