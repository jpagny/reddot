package com.elysium.reddot.ms.authentication.application.service;

import com.elysium.reddot.ms.authentication.application.exception.exception.IllegalStateApiException;
import com.elysium.reddot.ms.authentication.application.exception.exception.KeycloakApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationApplicationService {

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.resource}")
    private String userClientId;

    @Value("${keycloak.credentials.secret}")
    private String userClientSecret;

    @Value("${keycloak-admin.client-id}")
    private String adminClientId;

    @Value("${keycloak-admin.client-secret}")
    private String adminClientSecret;

    @Value("${keycloak-admin.username}")
    private String adminUsername;

    private final KeycloakBuilder keycloakBuilder;

    private Keycloak buildKeycloak(String username, String password) {
        return keycloakBuilder
                .grantType("password")
                .clientId(userClientId)
                .clientSecret(userClientSecret)
                .username(username)
                .password(password)
                .build();
    }

    private Keycloak buildKeycloakAdmin() {
        return keycloakBuilder
                .clientId(adminClientId)
                .clientSecret(adminClientSecret)
                .username(adminUsername)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public AccessTokenResponse getAccessToken(String username, String password) {
        log.debug("Fetching access token with username {}", username);

        AccessTokenResponse accessTokenResponse;

        try (Keycloak keycloak = buildKeycloak(username, password)) {
            log.debug("Keycloak client built successfully");

            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            log.debug("Access token retrieved successfully");

        } catch (NotAuthorizedException exception) {
            log.error("Error getting access token: NotAuthorizedException");
            Response response = exception.getResponse();
            throw new KeycloakApiException(response);

        } catch (java.lang.IllegalStateException exception) {
            log.error("Error build keycloak client: IllegalStateException: {}", exception.getMessage());
            throw new IllegalStateApiException("failed to build Keycloak client", exception);

        }

        return accessTokenResponse;
    }

    public void logout(HttpServletRequest request) {

        KeycloakSecurityContext keycloakSecurityContext = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

        if (keycloakSecurityContext != null) {
            String userId = keycloakSecurityContext.getToken().getSubject();
            log.debug("Trying to logout user with ID {}", userId);

            try (Keycloak keycloak = buildKeycloakAdmin()) {
                log.debug("User {} is being logged out", userId);

                keycloak.realm(keycloakRealm)
                        .users()
                        .get(userId)
                        .logout();
                log.debug("BEFORE : " + Arrays.toString(request.getSession().getValueNames()));
                request.getSession().invalidate();
                log.debug("AFTER : " + Arrays.toString(request.getSession().getValueNames()));
                log.debug("User {} has been logged out", userId);

            } catch (java.lang.IllegalStateException exception) {
                log.error("Error build keycloak client: IllegalStateException: {}", exception.getMessage());
                throw new IllegalStateApiException("failed to build Keycloak client", exception);

            }

        }

    }
}
