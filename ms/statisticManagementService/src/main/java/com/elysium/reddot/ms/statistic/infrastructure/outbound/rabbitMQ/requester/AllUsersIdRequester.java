package com.elysium.reddot.ms.statistic.infrastructure.outbound.rabbitMQ.requester;

import com.elysium.reddot.ms.statistic.infrastructure.constant.RabbitMQConstant;
import com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.received.response.ListUserIdsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * A component class for requesting all user IDs from a statistics service.
 **/
@RequiredArgsConstructor
@Slf4j
@Component
public class AllUsersIdRequester {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Fetches all user IDs from the statistics service.
     *
     * @return a list of user IDs.
     */
    public List<String> fetchAllUsers() {
        log.debug("Fetching all user IDs from the statistics service.");

        ListUserIdsResponse response = getAllUsers();
        if (response == null || response.getListUserIds() == null || response.getListUserIds().isEmpty()) {
            log.debug("No user IDs found.");
            return Collections.emptyList();
        }

        log.debug("Fetched {} user IDs.", response.getListUserIds().size());
        return response.getListUserIds();
    }

    private ListUserIdsResponse getAllUsers() {
        log.debug("Sending a request to fetch all user IDs.");

        ObjectMapper responseMapper = new ObjectMapper();

        try {

            byte[] replyBytes = (byte[]) rabbitTemplate.convertSendAndReceive(
                    RabbitMQConstant.EXCHANGE_STATISTIC_USER,
                    RabbitMQConstant.REQUEST_FETCH_ALL_USERS,
                    "hello"
            );

            if (replyBytes == null) {
                log.error("Received no response.");
                return null;
            }

            log.debug("Received a response. Converting to ListUserIdsResponse object.");
            return responseMapper.readValue(replyBytes, ListUserIdsResponse.class);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert to json", ex);

        }
    }


}
