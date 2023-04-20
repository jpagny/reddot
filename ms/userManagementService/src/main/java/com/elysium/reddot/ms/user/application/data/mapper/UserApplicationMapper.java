package com.elysium.reddot.ms.user.application.data.mapper;


import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.domain.model.UserModel;

public class UserApplicationMapper {

    private UserApplicationMapper() {
    }

    public static UserDTO toDTO(UserModel userModel) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userModel.getId());
        userDTO.setEmail(userModel.getEmail());
        userDTO.setUsername(userModel.getUsername());
        userDTO.setPassword(userModel.getPassword());
        return userDTO;
    }

    public static UserModel toModel(UserDTO userDTO) {
        return new UserModel(
                userDTO.getId(),
                userDTO.getEmail(),
                userDTO.getUsername(),
                userDTO.getPassword()
        );
    }

}
