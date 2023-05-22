package com.elysium.reddot.ms.authentication.application.service;

import com.elysium.reddot.ms.authentication.application.data.factory.KeycloakFactory;
import com.elysium.reddot.ms.authentication.infrastructure.exception.type.IllegalStateApiException;
import com.elysium.reddot.ms.authentication.infrastructure.exception.type.KeycloakApiException;
import com.elysium.reddot.ms.authentication.infrastructure.exception.type.LogoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationApplicationService {

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    private final KeycloakFactory keycloakFactory;

    /**
     * Retrieves an access token for the given username and password.
     *
     * <p>This method attempts to build a Keycloak client using the provided username and password.
     * If successful, it retrieves an access token from the Keycloak token manager. If not authorized,
     * a KeycloakApiException is thrown. If the Keycloak client cannot be built, an IllegalStateApiException
     * is thrown.
     *
     * @param username the username to be used to retrieve the access token
     * @param password the password to be used to retrieve the access token
     * @return the access token response from Keycloak
     * @throws KeycloakApiException     if a NotAuthorizedException is encountered when trying to get the access token
     * @throws IllegalStateApiException if an IllegalStateException is encountered when trying to build the Keycloak client
     */
    public AccessTokenResponse getAccessToken(String username, String password) {
        log.debug("Fetching access token with username {}", username);

        AccessTokenResponse accessTokenResponse;

        try (Keycloak keycloak = keycloakFactory.buildKeycloak(username, password)) {
            log.debug("Keycloak client built successfully");

            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            log.debug("Access token retrieved successfully");

        } catch (NotAuthorizedException exception) {
            log.error("Error getting access token: NotAuthorizedException");
            Response response = exception.getResponse();
            throw new KeycloakApiException(response);

        } catch (IllegalStateException exception) {
            log.error("Error build keycloak client: IllegalStateException: {}", exception.getMessage());
            throw new IllegalStateApiException("failed to build Keycloak client", exception);

        }

        return accessTokenResponse;
    }


    public ResponseEntity<String> logout(String token) {
        String logoutUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(logoutUrl, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new LogoutException(Objects.requireNonNull(response.getBody()));
        }

        return response;
    }


}
