package com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester;

import com.elysium.reddot.ms.statistic.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.received.response.CountMessagesByUserBetweenTwoDatesResponse;
import com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.request.CountMessagesByUserBetweenTwoDatesRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * A component class for requesting the count of messages by user between two dates from a statistics service.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class CountMessagesByUserBetweenTwoDatesRequester {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public Integer fetchCountMessageByUserBetweenTwoDate(String userId, LocalDateTime onStart, LocalDateTime onEnd) {
        log.debug("Fetching count of messages by user between two dates for user: {}, start: {}, end: {}", userId, onStart, onEnd);

        CountMessagesByUserBetweenTwoDatesResponse response = getCountMessages(userId, onStart, onEnd);

        log.debug("Received count of messages: {}", response.getCountMessagesTotal());

        return response.getCountMessagesTotal();
    }

    private CountMessagesByUserBetweenTwoDatesResponse getCountMessages(String userId, LocalDateTime onStart, LocalDateTime onEnd) {
        log.debug("Sending a request to fetch the count of messages by user between two dates.");

        CountMessagesByUserBetweenTwoDatesRequest request = new CountMessagesByUserBetweenTwoDatesRequest(userId, onStart, onEnd);
        String requestJson = buildJsonResponse(request);

        log.debug("Request JSON: {}", requestJson);

        assert requestJson != null;
        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                RabbitMQConstant.EXCHANGE_STATISTIC_MESSAGE,
                RabbitMQConstant.REQUEST_COUNT_MESSAGES_BY_USER_IN_RANGE_DATES_QUEUE,
                requestJson
        );

        if (replyBytes == null) {
            log.error("Received no response.");
            return new CountMessagesByUserBetweenTwoDatesResponse(-1);
        }

        log.debug("Received a response. Converting to CountMessagesByUserBetweenTwoDatesResponse object.");

        try {
            return objectMapper.readValue(replyBytes, CountMessagesByUserBetweenTwoDatesResponse.class);
        } catch (IOException ex) {
            log.error("Failed to convert response to JSON.", ex);
            throw new RuntimeException("Failed to convert to JSON.", ex);
        }

    }

    private String buildJsonResponse(Object response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            return null;
        }
    }

}
