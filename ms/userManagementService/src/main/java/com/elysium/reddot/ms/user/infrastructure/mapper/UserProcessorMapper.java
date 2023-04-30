package com.elysium.reddot.ms.user.infrastructure.mapper;

import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;


public class UserProcessorMapper {

    private UserProcessorMapper() {
    }

    public static UserDTO toDTO(UserRepresentation userRepresentation) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(userRepresentation.getId());
        userDTO.setEmail(userRepresentation.getEmail());
        userDTO.setUsername(userRepresentation.getUsername());
        userDTO.setEnabled(userRepresentation.isEnabled());
        userDTO.setMailVerified(userRepresentation.isEmailVerified());

        return userDTO;
    }

    public static UserRepresentation toUserRepresentation(UserDTO userDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setId(userDTO.getId());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setEnabled(userDTO.isEnabled());
        userRepresentation.setEmailVerified(userDTO.isMailVerified());

        return userRepresentation;
    }

}
