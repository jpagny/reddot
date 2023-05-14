package com.elysium.reddot.ms.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRabbitMQServiceTest {

    @Mock
    private Keycloak keycloak;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @InjectMocks
    private UserRabbitMQService userRabbitMQService;

    @BeforeEach
    public void setup() {
        when(keycloak.realm(any())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
    }

    @Test
    @DisplayName("Given users exist in the realm, when getAllUsers is called, then it should return the list of user IDs")
    public void givenUsersExist_whenGetAllUsersCalled_thenShouldReturnListOfUserIDs() {
        UserRepresentation user1 = new UserRepresentation();
        user1.setId("1");
        UserRepresentation user2 = new UserRepresentation();
        user2.setId("2");
        List<UserRepresentation> userList = Arrays.asList(user1, user2);
        when(usersResource.list()).thenReturn(userList);

        ArrayList<String> expectedUserIds = new ArrayList<>();
        expectedUserIds.add("1");
        expectedUserIds.add("2");

        ArrayList<String> actualUserIds = userRabbitMQService.getAllUsers();

        assertEquals(expectedUserIds, actualUserIds);
    }


}
