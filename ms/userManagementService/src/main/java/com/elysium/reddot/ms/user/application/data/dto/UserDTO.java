package com.elysium.reddot.ms.user.application.data.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String id;
    private String username;
    private String email;
    private boolean enabled;


}
