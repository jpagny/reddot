package com.elysium.reddot.ms.user.infrastructure.data.rabbitmq.send.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) for carrying a list of User IDs.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserIdsResponse {
    List<String> listUserIds;
}
