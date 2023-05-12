package com.elysium.reddot.ms.user.application.data.mapper;

import com.elysium.reddot.ms.user.domain.model.UserModel;
import org.keycloak.representations.idm.UserRepresentation;

/**
 * Mapper class for converting between UserModel and UserRepresentation.
 */
public class UserModelUserRepresentationMapper {

    /**
     * Converts a UserRepresentation to a UserModel.
     *
     * @param userRepresentation the UserRepresentation to convert
     * @return a UserModel with the same properties as the provided UserRepresentation
     */
    public static UserModel toUserModel(UserRepresentation userRepresentation) {
        return new UserModel(
                userRepresentation.getId(),
                userRepresentation.getUsername(),
                userRepresentation.getEmail(),
                userRepresentation.isEmailVerified(),
                "xxx"
        );

    }

    /**
     * Converts a UserModel to a UserRepresentation.
     *
     * @param userModel the UserModel to convert
     * @return a UserRepresentation with the same properties as the provided UserModel
     */
    public static UserRepresentation toUserRepresentation(UserModel userModel){
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userModel.getId());
        userRepresentation.setUsername(userModel.getUsername());
        userRepresentation.setEmail(userModel.getEmail());
        userRepresentation.setEmailVerified(userModel.isEmailVerified());

        return userRepresentation;
    }

}
