package com.elysium.reddot.ms.user.domain.port.inbound;

import com.elysium.reddot.ms.user.domain.model.UserModel;

public interface IUserManagementService {

    UserModel getUserById(long id);
    String createUser(UserModel userToCreate);

}
