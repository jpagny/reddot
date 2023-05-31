package com.elysium.reddot.ms.statistic.infrastructure.data.rabbitMQ.received.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * A Data Transfer Object (DTO) class that represents a response containing a list of user IDs.
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserIdsResponse {
    ArrayList<String> listUserIds;
}
