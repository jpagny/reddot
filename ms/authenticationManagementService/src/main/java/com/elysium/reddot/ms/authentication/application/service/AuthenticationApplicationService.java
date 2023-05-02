package com.elysium.reddot.ms.authentication.application.service;

import com.elysium.reddot.ms.authentication.application.exception.exception.IllegalStateApiException;
import com.elysium.reddot.ms.authentication.application.exception.exception.KeycloakApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationApplicationService {

    private final KeycloakBuilder keycloakBuilder;

    private Keycloak buildKeycloak(String username, String password) {
        return keycloakBuilder
                .username(username)
                .password(password)
                .build();
    }

    public AccessTokenResponse getAccessToken(String username, String password) {
        log.debug("Fetching access token with username {}", username);

        AccessTokenResponse accessTokenResponse;

        try (Keycloak keycloak = buildKeycloak(username, password)) {
            log.debug("Keycloak client built successfully");

            try {
                accessTokenResponse = keycloak.tokenManager().getAccessToken();
                log.debug("Access token retrieved successfully");

            } catch (NotAuthorizedException exception) {
                log.error("Error getting access token: NotAuthorizedException");
                Response response = exception.getResponse();
                throw new KeycloakApiException(response);

            }

        } catch (java.lang.IllegalStateException exception) {
            log.error("Error build keycloak client: IllegalStateException: {}", exception.getMessage());
            throw new IllegalStateApiException("failed to build Keycloak client", exception);

        }

        return accessTokenResponse;
    }


}
