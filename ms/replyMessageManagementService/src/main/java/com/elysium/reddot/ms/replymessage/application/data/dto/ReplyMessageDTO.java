package com.elysium.reddot.ms.replymessage.application.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for reply message data.
 * This is used to expose a standard and consistent structure for reply messages in API responses and requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyMessageDTO {


    @ApiModelProperty("The unique identifier of the reply message.")
    private Long id;

    @ApiModelProperty("The content of the reply message.")
    private String content;

    @ApiModelProperty("The unique identifier of the parent message this is a reply to.")
    private Long parentMessageID;

    @ApiModelProperty("The unique identifier of the user who posted the reply message.")
    private String userId;

    @ApiModelProperty("The date and time when the reply message was created.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @ApiModelProperty("The date and time when the reply message was last updated.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * Constructs a ReplyMessageDTO with only content, parent message ID, and user ID specified.
     * This can be used when creating a new reply message.
     *
     * @param content the content of the reply message
     * @param parentMessageID the unique identifier of the parent message this is a reply to
     * @param userId the unique identifier of the user who is posting the reply message
     */
    public ReplyMessageDTO(String content, Long parentMessageID, String userId){
        this.content = content;
        this.parentMessageID = parentMessageID;
        this.userId = userId;
    }

}
