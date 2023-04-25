package com.elysium.reddot.ms.user.domain.port.outbound;

import com.elysium.reddot.ms.user.domain.model.UserModel;

import java.util.Optional;

public interface IUserRepository {

    Optional<UserModel> findTopicById(long id);

    UserModel createTopic(UserModel userModel);

}
