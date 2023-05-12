package com.elysium.reddot.ms.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service class for managing users with RabbitMQ integration.
 * This class provides methods to interact with Keycloak and retrieve user information.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserRabbitMQService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    /**
     * Retrieves the UsersResource for the configured Keycloak realm.
     *
     * @return the UsersResource instance
     */
    private UsersResource getUsersIdResource() {
        RealmResource realmResource = keycloak.realm(keycloakRealm);
        return realmResource.users();
    }

    /**
     * Retrieves a list of all user IDs from Keycloak.
     *
     * @return an ArrayList containing the IDs of all users
     */
    public ArrayList<String> getAllUsers() {
        log.debug("Fetching list users");

        ArrayList<String> listUserId = new ArrayList<>();

        for (UserRepresentation userRepresentation : getUsersIdResource().list()) {
            listUserId.add(userRepresentation.getId());
        }

        log.info("Total number of users: {}", listUserId.size());
        log.debug("List of user IDs: {}", listUserId);

        return listUserId;
    }

}
