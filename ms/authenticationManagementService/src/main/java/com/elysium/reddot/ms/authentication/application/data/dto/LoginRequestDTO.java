package com.elysium.reddot.ms.authentication.application.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for handling login requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @ApiModelProperty(notes = "The username of the user.")
    private String username;

    @ApiModelProperty(notes = "The password of the user.")
    private String password;

}
