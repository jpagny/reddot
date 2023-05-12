package com.elysium.reddot.ms.user.domain.port.inbound;

import com.elysium.reddot.ms.user.infrastructure.exception.type.KeycloakApiException;
import com.elysium.reddot.ms.user.domain.model.UserModel;

/**
 * Interface for User Management Service.
 */
public interface IUserManagementService {

    /**
     * Creates a new user.
     *
     * This method validates the provided UserModel, converts it to a UserRepresentation,
     * and then sends a request to the Keycloak server to create a new user with the provided details.
     * The method logs the beginning and end of the process, and any exceptions that occur are properly handled.
     *
     * @param userModelToCreate the UserModel to be created
     * @return a UserModel that represents the user that was created on the Keycloak server
     * @throws KeycloakApiException if the server returns an unsuccessful response
     */
    UserModel createUser(UserModel userModelToCreate);

}
