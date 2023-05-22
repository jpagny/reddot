package com.elysium.reddot.ms.replymessage.application.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyMessageDTO {

    private Long id;
    private String content;
    private Long parentMessageID;
    private String userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public ReplyMessageDTO(String content, Long parentMessageID, String userId){
        this.content = content;
        this.parentMessageID = parentMessageID;
        this.userId = userId;
    }

}
