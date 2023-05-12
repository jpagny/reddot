package com.elysium.reddot.ms.user.infrastructure.data.rabbitMQ.send.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserIdsResponse {
    ArrayList<String> listUserIds;
}
