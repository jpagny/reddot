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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRabbitMQService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    private UsersResource getUsersIdResource() {
        RealmResource realmResource = keycloak.realm(keycloakRealm);
        return realmResource.users();
    }

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
