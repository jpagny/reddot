package com.elysium.reddot.ms.user.application.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) class representing user data.
 * This class provides a convenient way to transfer user information between different layers or components of the application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String password;
    private String email;
    private boolean mailVerified;
}
