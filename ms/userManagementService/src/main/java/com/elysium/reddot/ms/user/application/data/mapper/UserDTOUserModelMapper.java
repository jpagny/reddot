package com.elysium.reddot.ms.user.application.data.mapper;

import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.domain.model.UserModel;

/**
 * Mapper class for converting between UserModel and UserDTO.
 */
public class UserDTOUserModelMapper {

    private UserDTOUserModelMapper(){}

    /**
     * Converts a UserModel object to a UserDTO object.
     *
     * @param userModel the UserModel object to be converted
     * @return the corresponding UserDTO object
     */
    public static UserDTO toUserDTO(UserModel userModel) {
        String id = userModel.getId();
        String username = userModel.getUsername();
        String password = userModel.getPassword();
        String email = userModel.getEmail();
        boolean mailVerified = userModel.isEmailVerified();

        return new UserDTO(id, username, password, email, mailVerified);
    }

    /**
     * Converts a UserDTO object to a UserModel object.
     *
     * @param userDTO the UserDTO object to be converted
     * @return the corresponding UserModel object
     */
    public static UserModel toUserModel(UserDTO userDTO) {
        String id = userDTO.getId();
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        String email = userDTO.getEmail();
        boolean mailVerified = userDTO.isMailVerified();

        return new UserModel(id, username, email, mailVerified, password);
    }

}
