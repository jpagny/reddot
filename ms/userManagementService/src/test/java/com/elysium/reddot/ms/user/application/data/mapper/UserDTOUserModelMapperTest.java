package com.elysium.reddot.ms.user.application.data.mapper;

import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.domain.model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserDTOUserModelMapperTest {

    @Test
    @DisplayName("given UserModel, when toUserDTO is called, then return correct UserDTO")
    void givenUserModel_whenToUserDtoCalled_thenReturnCorrectUserDto() {
        // given
        UserModel userModel = new UserModel();
        userModel.setId("testId");
        userModel.setUsername("testUsername");
        userModel.setPassword("testPassword");
        userModel.setEmail("testEmail");
        userModel.setEmailVerified(true);

        // when
        UserDTO userDTO = UserDTOUserModelMapper.toUserDTO(userModel);

        // then
        assertThat(userDTO.getId()).isEqualTo(userModel.getId());
        assertThat(userDTO.getUsername()).isEqualTo(userModel.getUsername());
        assertThat(userDTO.getPassword()).isEqualTo(userModel.getPassword());
        assertThat(userDTO.getEmail()).isEqualTo(userModel.getEmail());
        assertThat(userDTO.isMailVerified()).isEqualTo(userModel.isEmailVerified());
    }

    @Test
    @DisplayName("given UserDTO, when toUserModel is called, then return correct UserModel")
    void givenUserDto_whenToUserModelCalled_thenReturnCorrectUserModel() {
        // given
        UserDTO userDTO = new UserDTO("testId", "testUsername", "testPassword", "testEmail", true);

        // when
        UserModel userModel = UserDTOUserModelMapper.toUserModel(userDTO);

        // then
        assertThat(userModel.getId()).isEqualTo(userDTO.getId());
        assertThat(userModel.getUsername()).isEqualTo(userDTO.getUsername());
        assertThat(userModel.getPassword()).isEqualTo(userDTO.getPassword());
        assertThat(userModel.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(userModel.isEmailVerified()).isEqualTo(userDTO.isMailVerified());
    }



}
