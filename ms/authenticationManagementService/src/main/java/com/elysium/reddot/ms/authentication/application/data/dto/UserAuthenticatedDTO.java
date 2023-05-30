package com.elysium.reddot.ms.authentication.application.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for carrying authentication details of a User.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticatedDTO {

    @ApiModelProperty(notes = "The bearer token representing the authenticated user.")
    private String baerToken;
}
