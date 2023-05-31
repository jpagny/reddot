package com.elysium.reddot.ms.statistic.infrastructure.inbound.rest.processor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * A simple holder for Processor objects that are used for handling statistics.
 **/
@AllArgsConstructor
@Getter
@Component
public class StatisticProcessorHolder {
    private final GetMessageCountByUserFromDateProcessor getMessageCountByUserFromDateProcessor;
}