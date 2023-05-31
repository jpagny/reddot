package com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester;

import com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.received.response.CountMessagesByUserBetweenTwoDatesResponse;
import com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.request.CountRepliesMessageByUserBetweenTwoDatesRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * A component class for requesting the count of replies messages by user between two dates from a statistics service.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class CountRepliesMessageByUserBetweenTwoDatesRequester {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public static final String STATISTIC_REPLYMESSAGE_EXCHANGE = "statisticReplyMessageExchange";
    public static final String STATISTIC_COUNT_REPLIES_MESSAGE_BY_USER_BETWEEN_TWO_DATES_REQUEST_ROUTING_KEY = "count.replyMessage.user.dates.request";

    public Integer fetchCountRepliesMessageByUserBetweenTwoDate(String userId, LocalDateTime onStart, LocalDateTime onEnd) {
        log.debug("Fetching count of replies messages by user between two dates for user: {}, start: {}, end: {}", userId, onStart, onEnd);

        CountMessagesByUserBetweenTwoDatesResponse response = getCountMessages(userId, onStart, onEnd);

        log.debug("Received count of replies messages: {}", response.getCountMessagesTotal());

        return response.getCountMessagesTotal();
    }

    private CountMessagesByUserBetweenTwoDatesResponse getCountMessages(String userId, LocalDateTime onStart, LocalDateTime onEnd) {
        log.debug("Sending a request to fetch the count of replies messages by user between two dates.");

        CountRepliesMessageByUserBetweenTwoDatesRequest request = new CountRepliesMessageByUserBetweenTwoDatesRequest(userId, onStart, onEnd);
        String requestJson = buildJsonResponse(request);

        log.debug("Request JSON: {}", requestJson);


        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                STATISTIC_REPLYMESSAGE_EXCHANGE,
                STATISTIC_COUNT_REPLIES_MESSAGE_BY_USER_BETWEEN_TWO_DATES_REQUEST_ROUTING_KEY,
                requestJson
        );

        if (replyBytes == null) {
            log.error("Received no response.");
            return null;
        }

        log.debug("Received a response. Converting to CountMessagesByUserBetweenTwoDatesResponse object.");

        try {
            return objectMapper.readValue(replyBytes, CountMessagesByUserBetweenTwoDatesResponse.class);

        } catch (IOException ex) {
            log.error("Failed to convert response to JSON.", ex);
            throw new RuntimeException("Failed to convert to json", ex);

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
