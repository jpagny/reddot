package com.elysium.reddot.ms.topic.infrastructure.data.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TopicExistsResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long topicId;
    private boolean exists;
}
