package com.elysium.reddot.ms.authentication.application.service;

import com.elysium.reddot.ms.authentication.application.data.factory.KeycloakFactory;
import com.elysium.reddot.ms.authentication.configuration.TestConfig;
import com.elysium.reddot.ms.authentication.infrastructure.exception.type.IllegalStateApiException;
import com.elysium.reddot.ms.authentication.infrastructure.exception.type.KeycloakApiException;
import com.elysium.reddot.ms.authentication.infrastructure.exception.type.LogoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.NotAuthorizedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
class AuthenticationApplicationServiceTest {

    public AuthenticationApplicationService authenticationApplicationService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private KeycloakFactory keycloakFactory;

    @Mock
    private Keycloak keycloak;

    @BeforeEach
    void setUp() {
        authenticationApplicationService =
                new AuthenticationApplicationService(keycloakFactory, restTemplate);
    }

    private final String authServerUrl = "http://auth-server";
    private final String realm = "test-realm";

    @Test
    @DisplayName("given valid user credentials, when getAccessToken is called, then return valid AccessTokenResponse")
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

    @Test
    @DisplayName("given a valid token, when logout is invoked, then returns success")
    void givenValidToken_whenLogoutInvoked_thenReturnSuccess() {
        // given
        ReflectionTestUtils.setField(authenticationApplicationService, "authServerUrl", "http://auth-server");
        ReflectionTestUtils.setField(authenticationApplicationService, "realm", "test-realm");

        String token = "test-token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String logoutUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

        ResponseEntity<String> mockResponse = new ResponseEntity<>("Success", HttpStatus.OK);

        // mock
        when(restTemplate.exchange(logoutUrl, HttpMethod.POST, request, String.class)).thenReturn(mockResponse);

        // when
        ResponseEntity<String> response = authenticationApplicationService.logout(token);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody());
    }

    @Test
    @DisplayName("given an invalid token, when logout is invoked, then throws LogoutException")
    void givenInvalidToken_whenLogoutInvoked_thenThrowsLogoutException() {
        // given
        ReflectionTestUtils.setField(authenticationApplicationService, "authServerUrl", "http://auth-server");
        ReflectionTestUtils.setField(authenticationApplicationService, "realm", "test-realm");

        String token = "invalid-token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String logoutUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

        ResponseEntity<String> mockResponse = new ResponseEntity<>("Failure", HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(logoutUrl, HttpMethod.POST, request, String.class)).thenReturn(mockResponse);

        // when & then
        Exception exception = assertThrows(LogoutException.class, () -> {
            authenticationApplicationService.logout(token);
        });

        assertEquals("Fail to logout : Failure", exception.getMessage());
    }

}
