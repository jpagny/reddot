package com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester;

import com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.received.response.ListUserIdsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class AllUsersIdRequester {

    private final RabbitTemplate rabbitTemplate;

    public static final String STATISTIC_EXCHANGE = "statisticUserExchange";
    public static final String STATISTIC_ALL_USERS_REQUEST_ROUTING_KEY = "user.all.request";

    public List<String> fetchAllUsers() {
        ListUserIdsResponse response = getAllUsers();
        if (response == null || response.getListUserIds() == null || response.getListUserIds().isEmpty()) {
            return Collections.emptyList();
        }

        return response.getListUserIds();
    }

    private ListUserIdsResponse getAllUsers() {
        ObjectMapper responseMapper = new ObjectMapper();

        try {

            byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                    STATISTIC_EXCHANGE,
                    STATISTIC_ALL_USERS_REQUEST_ROUTING_KEY,
                    "hello"
            );

            if (replyBytes == null) {
                // Handle this situation
                return null;
            }

            return responseMapper.readValue(replyBytes, ListUserIdsResponse.class);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert to json", ex);

        }
    }


}
