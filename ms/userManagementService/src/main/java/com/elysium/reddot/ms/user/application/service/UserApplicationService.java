package com.elysium.reddot.ms.user.application.service;

import com.elysium.reddot.ms.user.application.exception.exception.KeycloakException;
import com.elysium.reddot.ms.user.domain.port.inbound.IUserManagementService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserApplicationService implements IUserManagementService {

    @NonNull
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    private UsersResource getUsersResource() {
        RealmResource realmResource = keycloak.realm(keycloakRealm);
        return realmResource.users();
    }

    @Override
    public UserRepresentation createUser(UserRepresentation userRepresentation) {

        try (Response response = getUsersResource().create(userRepresentation)) {

            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                return getUsersResource().get(userId).toRepresentation();

            } else {
                throw new KeycloakException("Error creating user. Status: " + response.getStatus());

            }
        }
    }


}