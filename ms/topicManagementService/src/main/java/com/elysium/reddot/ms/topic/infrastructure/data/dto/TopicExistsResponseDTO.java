package com.elysium.reddot.ms.topic.infrastructure.data.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TopicExistsResponseDTO implements Serializable {
    private boolean exists;
}
