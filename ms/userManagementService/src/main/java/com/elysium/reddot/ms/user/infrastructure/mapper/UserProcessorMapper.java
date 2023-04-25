package com.elysium.reddot.ms.user.infrastructure.mapper;

import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.domain.model.UserModel;


public class UserProcessorMapper {

    private UserProcessorMapper() {
    }

    public static UserDTO toDTO(UserModel topicModel) {
        UserDTO topicDTO = new UserDTO();
        return topicDTO;
    }


    public static UserModel toModel(UserDTO userDTO) {
        return new UserModel(
        );
    }

}
