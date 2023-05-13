package com.elysium.reddot.ms.user.application.service;

import com.elysium.reddot.ms.user.domain.model.UserModel;
import com.elysium.reddot.ms.user.domain.port.inbound.IUserManagementService;
import com.elysium.reddot.ms.user.domain.service.IUserDomainService;
import com.elysium.reddot.ms.user.domain.service.UserDomainServiceImpl;
import com.elysium.reddot.ms.user.infrastructure.exception.type.KeycloakApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserApplicationServiceImplTest {

    private IUserManagementService userApplicationService;

    @Mock
    private Keycloak keycloak;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UserResource userResource;

    @Mock
    private UsersResource usersResource;

    @Mock
    private Response response;


    @BeforeEach
    public void setup() {

        IUserDomainService userDomainService = new UserDomainServiceImpl();
        userApplicationService = new UserApplicationServiceImpl(userDomainService, keycloak);

        ReflectionTestUtils.setField(userApplicationService, "activeProfile", "dev");

        when(keycloak.realm(any())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
    }

    @Test
    @DisplayName("Given a valid user, when createUser is called, then return the created user")
    void givenValidUser_whenCreationRequested_thenUserIsCreated() {
        // given
        UserModel userModel = new UserModel();
        userModel.setUsername("username");
        userModel.setPassword("Passw0rd&");
        userModel.setEmail("email@example.com");
        userModel.setEmailVerified(true);

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userModel.getUsername());
        userRepresentation.setEmail(userModel.getEmail());
        userRepresentation.setEmailVerified(true);

        URI uri = UriBuilder.fromPath("http://example.com/users/123").build();
        Response response = Response.status(Response.Status.CREATED).location(uri).entity("userId").build();

        // mock
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(usersResource.get(anyString())).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);

        // when
        UserModel result = userApplicationService.createUser(userModel);

        // then
        assertNotNull(result);
        assertEquals(userModel.getUsername(), result.getUsername());
        assertEquals(userModel.getEmail(), result.getEmail());
        verify(usersResource, times(1)).create(any(UserRepresentation.class));
        verify(usersResource, times(1)).get(anyString());
    }

    @Test
    @DisplayName("Given a user creation failure, when createUser is called, then it should throw KeycloakApiException")
    void givenUserCreationFailure_whenAttemptingToCreateUser_thenKeycloakApiExceptionIsThrown() {
        // given
        UserModel userModel = new UserModel();
        userModel.setUsername("username");
        userModel.setPassword("Pass0rd&");
        userModel.setEmail("email@example.com");

        Response response = Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();

        // mock
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);

        // when
        Throwable exception = assertThrows(KeycloakApiException.class, () -> userApplicationService.createUser(userModel));

        // then
        assertTrue(exception.getMessage().contains("Bad Request"));
    }

}
