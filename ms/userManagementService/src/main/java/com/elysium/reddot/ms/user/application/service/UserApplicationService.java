package com.elysium.reddot.ms.user.application.service;

import com.elysium.reddot.ms.user.application.exception.exception.KeycloakApiException;
import com.elysium.reddot.ms.user.domain.port.inbound.IUserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserApplicationService implements IUserManagementService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    private UsersResource getUsersResource() {
        RealmResource realmResource = keycloak.realm(keycloakRealm);
        return realmResource.users();
    }

    @Override
    public UserRepresentation createUser(UserRepresentation userRepresentation) {

        log.debug("Creating new user with userName '{}', email '{}",
                userRepresentation.getUsername(),
                userRepresentation.getEmail()
        );

        try (Response response = getUsersResource().create(userRepresentation)) {

            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                String userId = extractUserIdFromResponse(response);
                UserResource userResource = getUsersResource().get(userId);
                UserRepresentation userRepresentationCreated = userResource.toRepresentation();

                log.info("Successfully created user with userName '{}', email '{}",
                        userRepresentationCreated.getUsername(),
                        userRepresentationCreated.getEmail()
                );

                return userRepresentationCreated;

            } else {
                throw new KeycloakApiException(response);

            }
        }
    }


    private String extractUserIdFromResponse(Response response) {
        return response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
    }


}