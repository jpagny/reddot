package com.elysium.reddot.ms.board.infrastructure.data.dto;

import lombok.Data;

@Data
public class TopicExistsResponseDTO  {
    private Long topicId;
    private boolean exists;
}