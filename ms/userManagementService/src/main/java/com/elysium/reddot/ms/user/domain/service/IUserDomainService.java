package com.elysium.reddot.ms.user.domain.service;

import com.elysium.reddot.ms.user.domain.exception.BadValueException;
import com.elysium.reddot.ms.user.domain.model.UserModel;

/**
 * Interface for User Domain Service.
 */
public interface IUserDomainService {

    /**
     * Validates a user model for creation.
     * Checks the username, email, and password for validity.
     *
     * @param userModel the user model to be validated
     * @throws BadValueException if the user model is not valid
     */
    void validateUserForCreation(UserModel userModel);

}
