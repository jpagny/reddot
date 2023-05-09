package com.elysium.reddot.ms.statistic.infrastructure.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllUserIdResponseDTO {
    private static final long serialVersionUID = 1L;
    private ArrayList<String> userIdList;
}
