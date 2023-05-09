package com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester;

import com.elysium.reddot.ms.statistic.infrastructure.data.dto.AllUserIdResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
public class AllUsersIdRequester {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public static final String STATISTIC_EXCHANGE = "statisticExchange";
    public static final String STATISTIC_ALL_USERS_REQUEST_ROUTING_KEY = "user.all.request";

    public ArrayList<String> fetchAllUsers() {
        AllUserIdResponseDTO response = getAllUsers();

        if (response != null && response.getUserIdList() == null || Objects.requireNonNull(response).getUserIdList().isEmpty()) {
            //throw new ResourceNotFoundException("Board id", String.valueOf(threadId));
        }

        return response.getUserIdList();
    }

    private AllUserIdResponseDTO getAllUsers() {
        byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                STATISTIC_EXCHANGE,
                STATISTIC_ALL_USERS_REQUEST_ROUTING_KEY
        );
        try {
            return objectMapper.readValue(replyBytes, AllUserIdResponseDTO.class);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert to json", ex);

        }
    }



}
