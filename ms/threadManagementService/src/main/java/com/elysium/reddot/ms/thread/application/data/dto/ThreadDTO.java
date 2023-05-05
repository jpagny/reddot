package com.elysium.reddot.ms.thread.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadDTO {
    private Long id;
    private String name;
    private String label;
    private String description;
    private Long boardId;
    private String userId;
}
