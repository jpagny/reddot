package com.elysium.reddot.ms.authentication.application.service;

import com.elysium.reddot.ms.authentication.application.data.factory.KeycloakFactory;
import com.elysium.reddot.ms.authentication.infrastructure.exception.type.IllegalStateApiException;
import com.elysium.reddot.ms.authentication.infrastructure.exception.type.KeycloakApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.NotAuthorizedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationApplicationServiceTest {

    @InjectMocks
    public AuthenticationApplicationService authenticationApplicationService;

    @Mock
    KeycloakFactory keycloakFactory;

    @Mock
    Keycloak keycloak;

    @Test
    @DisplayName("Given valid user credentials, when getAccessToken is called, then return valid AccessTokenResponse")
    void givenValidCredentials_whenGetAccessToken_thenReturnsAccessToken() {
        // given
        String testUsername = "username";
        String testPassword = "password";

        AccessTokenResponse mockResponse = mock(AccessTokenResponse.class);
        TokenManager tokenManager = mock(TokenManager.class);

        // mock
        when(keycloakFactory.buildKeycloak(testUsername, testPassword)).thenReturn(keycloak);
        when(keycloak.tokenManager()).thenReturn(tokenManager);
        when(tokenManager.getAccessToken()).thenReturn(mockResponse);

        // when
        AccessTokenResponse result = authenticationApplicationService.getAccessToken(testUsername, testPassword);

        // then
        assertEquals(mockResponse, result);
        verify(tokenManager, times(1)).getAccessToken();
    }

    @Test
    @DisplayName("given unauthorized user credentials, when getAccessToken is called, then KeycloakApiException is thrown")
    void givenUnauthorizedCredentials_whenGetAccessToken_thenKeycloakApiExceptionIsThrown() {
        // given
        String testUsername = "username";
        String testPassword = "password";
        TokenManager tokenManager = mock(TokenManager.class);

        // mock
        when(keycloakFactory.buildKeycloak(testUsername, testPassword)).thenReturn(keycloak);
        when(keycloak.tokenManager()).thenReturn(tokenManager);
        when(tokenManager.getAccessToken()).thenThrow(new NotAuthorizedException("Not authorized"));

        // when && then
        assertThrows(KeycloakApiException.class, () -> authenticationApplicationService.getAccessToken(testUsername, testPassword));
        verify(tokenManager, times(1)).getAccessToken();
    }

    @Test
    @DisplayName("given IllegalStateException when building Keycloak, when getAccessToken is called, then IllegalStateApiException is thrown")
    void givenIllegalStateException_whenGetAccessToken_thenIllegalStateApiExceptionIsThrown() {
        // given
        String testUsername = "username";
        String testPassword = "password";

        // mock
        when(keycloakFactory.buildKeycloak(testUsername, testPassword)).thenThrow(new IllegalStateException("Failed to build Keycloak"));

        // when && then
        assertThrows(IllegalStateApiException.class, () ->
                authenticationApplicationService.getAccessToken(testUsername, testPassword)
        );
    }



}
