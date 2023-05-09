package com.elysium.reddot.ms.replymessage.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyMessageDTO {

    private Long id;
    private String content;
    private Long parentMessageID;
    private String userId;

}
