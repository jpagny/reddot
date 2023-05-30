package com.elysium.reddot.ms.message.application.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) class for transferring message data between different application layers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Data Transfer Object for Message")
public class MessageDTO {

    @ApiModelProperty(notes = "The unique ID of the message")
    private Long id;

    @ApiModelProperty(notes = "The content of the message")
    private String content;

    @ApiModelProperty(notes = "The ID of the thread that the message belongs to")
    private Long threadId;

    @ApiModelProperty(notes = "The ID of the user who created the message")
    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "The creation timestamp of the message", example = "2023-05-29 13:00:00")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "The last update timestamp of the message", example = "2023-05-29 13:00:00")
    private LocalDateTime updatedAt;

    /**
     * Constructor for creating a new MessageDTO with a given id, content, threadId, and userId.
     */
    public MessageDTO(Long id, String content, Long threadId, String userId) {
        this.id = id;
        this.content = content;
        this.threadId = threadId;
        this.userId = userId;
    }

    /**
     * Constructor for creating a new MessageDTO with a given content and threadId.
     */
    public MessageDTO(String content, Long threadId) {
        this.content = content;
        this.threadId = threadId;
    }

    /**
     * Constructor for creating a new MessageDTO with a given content.
     */
    public MessageDTO(String content) {
        this.content = content;
    }

    /**
     * Constructor for creating a new MessageDTO with a given content, threadId, and userId.
     */
    public MessageDTO(String content, Long threadId, String userId) {
        this.content = content;
        this.threadId = threadId;
        this.userId = userId;
    }

}
