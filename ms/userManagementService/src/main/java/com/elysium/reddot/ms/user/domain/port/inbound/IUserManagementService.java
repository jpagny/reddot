package com.elysium.reddot.ms.user.domain.port.inbound;

import org.keycloak.representations.idm.UserRepresentation;

public interface IUserManagementService {

    UserRepresentation createUser(UserRepresentation userToCreate);

}
