package com.elysium.reddot.ms.user.application.port.inbound;

import com.elysium.reddot.ms.user.application.data.dto.UserDTO;

import java.util.Optional;

interface IUserManagement {

    Optional<UserDTO> getUserById(Long id);

    UserDTO createUser(UserDTO user);



}
