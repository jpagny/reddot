package com.elysium.reddot.ms.message.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Long id;
    private String content;
    private Long messageId;
    private Long threadId;
    private String userId;

}
