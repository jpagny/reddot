package com.elysium.reddot.ms.user.application.data.dto;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(notes = "The unique id of the User.")
    private String id;

    @ApiModelProperty(notes = "The username of the User.")
    private String username;

    @ApiModelProperty(notes = "The password of the User.")
    private String password;

    @ApiModelProperty(notes = "The email of the User.")
    private String email;

    @ApiModelProperty(notes = "Indicates whether the User's email has been verified.")
    private boolean mailVerified;

    /**
     * Constructs a new UserDTO with username, email, and password.
     *
     * @param username The username of the User.
     * @param email The email of the User.
     * @param password The password of the User.
     */
    public UserDTO(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
}


