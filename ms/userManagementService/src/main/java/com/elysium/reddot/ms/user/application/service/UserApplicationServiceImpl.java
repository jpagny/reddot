package com.elysium.reddot.ms.user.application.service;

import com.elysium.reddot.ms.user.application.data.mapper.UserModelUserRepresentationMapper;
import com.elysium.reddot.ms.user.infrastructure.exception.type.KeycloakApiException;
import com.elysium.reddot.ms.user.domain.model.UserModel;
import com.elysium.reddot.ms.user.domain.port.inbound.IUserManagementService;
import com.elysium.reddot.ms.user.domain.service.IUserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserApplicationServiceImpl implements IUserManagementService {

    private final IUserDomainService userDomainService;
    private final Keycloak keycloak;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    private UsersResource getUsersResource() {
        RealmResource realmResource = keycloak.realm(keycloakRealm);
        return realmResource.users();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserModel createUser(UserModel userModel) {

        log.debug("Creating new user with userName '{}', email '{}",
                userModel.getUsername(),
                userModel.getEmail()
        );

        userDomainService.validateUserForCreation(userModel);

        UserRepresentation userRepresentation = UserModelUserRepresentationMapper.toUserRepresentation(userModel);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userModel.getPassword());
        credential.setTemporary(false);

        userRepresentation.setCredentials(Collections.singletonList(credential));

        if (activeProfile.matches("(dev|demo)")) {
            userRepresentation.setEnabled(true);
            userRepresentation.setEmailVerified(true);
        }

        try (Response response = getUsersResource().create(userRepresentation)) {

            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                String userId = extractUserIdFromResponse(response);
                UserResource userResource = getUsersResource().get(userId);
                UserRepresentation userRepresentationCreated = userResource.toRepresentation();

                UserModel userModelCreated = UserModelUserRepresentationMapper.toUserModel(userRepresentationCreated);

                log.info("Successfully created user with userName '{}', email '{}",
                        userModelCreated.getUsername(),
                        userModelCreated.getEmail()
                );

                return userModelCreated;

            } else {
                throw new KeycloakApiException(response);

            }
        }
    }


    /**
     * Extracts the user ID from the given Response object.
     * The user ID is extracted from the path of the response's location.
     *
     * @param response the Response object from which to extract the user ID
     * @return the extracted user ID
     */
    private String extractUserIdFromResponse(Response response) {
        Path path = Paths.get(response.getLocation().getPath());
        return path.getFileName().toString();
    }


}