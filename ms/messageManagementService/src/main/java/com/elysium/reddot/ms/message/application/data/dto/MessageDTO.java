package com.elysium.reddot.ms.message.application.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Long id;
    private String content;
    private Long threadId;
    private String userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public MessageDTO(Long id, String content, Long threadId, String userId){
        this.id = id;
        this.content = content;
        this.threadId = threadId;
        this.userId = userId;
    }

    public MessageDTO(String content, Long threadId, String userId){
        this.content = content;
        this.threadId = threadId;
        this.userId = userId;
    }

}
