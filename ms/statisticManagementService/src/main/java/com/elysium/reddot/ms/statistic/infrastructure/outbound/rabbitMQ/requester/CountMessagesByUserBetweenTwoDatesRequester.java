package com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester;

import com.elysium.reddot.ms.statistic.infrastructure.data.dto.CountMessageByUserBetweenTwoDatesResponse;
import com.elysium.reddot.ms.statistic.infrastructure.data.request.CountMessageByUserBetweenTwoDatesRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CountMessagesByUserBetweenTwoDatesRequester {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public static final String STATISTIC_EXCHANGE = "statisticExchange";
    public static final String STATISTIC_COUNT_MESSAGE_BY_USER_BETWEEN_TWO_DATES_REQUEST_ROUTING_KEY = "count.message.user.dates.request";

    public Integer fetchCountMessageByUserBetweenTwoDate(String userId, LocalDateTime onStart, LocalDateTime onEnd) {
        CountMessageByUserBetweenTwoDatesResponse response = getCountMessages(userId, onStart, onEnd);

        return response.getCountMessages();
    }

    private CountMessageByUserBetweenTwoDatesResponse getCountMessages(String userId, LocalDateTime onStart, LocalDateTime onEnd) {

        CountMessageByUserBetweenTwoDatesRequest request = new CountMessageByUserBetweenTwoDatesRequest(userId, onStart, onEnd);

        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                STATISTIC_EXCHANGE,
                STATISTIC_COUNT_MESSAGE_BY_USER_BETWEEN_TWO_DATES_REQUEST_ROUTING_KEY,
                request
        );
        try {
            return objectMapper.readValue(replyBytes, CountMessageByUserBetweenTwoDatesResponse.class);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert to json", ex);

        }
    }

}
