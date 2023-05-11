package com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester;

import com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.received.response.CountMessageByUserBetweenTwoDatesResponse;
import com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.request.CountMessageByUserBetweenTwoDatesRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Slf4j
public class CountMessagesByUserBetweenTwoDatesRequester {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public static final String STATISTIC_EXCHANGE = "statisticMessageExchange";
    public static final String STATISTIC_COUNT_MESSAGE_BY_USER_BETWEEN_TWO_DATES_REQUEST_ROUTING_KEY = "count.message.user.dates.request";

    public Integer fetchCountMessageByUserBetweenTwoDate(String userId, LocalDateTime onStart, LocalDateTime onEnd) {
        CountMessageByUserBetweenTwoDatesResponse response = getCountMessages(userId, onStart, onEnd);
        return response.getCountMessagesTotal();
    }

    private CountMessageByUserBetweenTwoDatesResponse getCountMessages(String userId, LocalDateTime onStart, LocalDateTime onEnd) {
        CountMessageByUserBetweenTwoDatesRequest request = new CountMessageByUserBetweenTwoDatesRequest(userId, onStart, onEnd);
        String requestJson = buildJsonResponse(request);
        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                STATISTIC_EXCHANGE,
                STATISTIC_COUNT_MESSAGE_BY_USER_BETWEEN_TWO_DATES_REQUEST_ROUTING_KEY,
                requestJson
        );
        try {
            return objectMapper.readValue(replyBytes, CountMessageByUserBetweenTwoDatesResponse.class);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert to json", ex);

        }
    }

    private String buildJsonResponse(Object response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            return null;
        }
    }

}
