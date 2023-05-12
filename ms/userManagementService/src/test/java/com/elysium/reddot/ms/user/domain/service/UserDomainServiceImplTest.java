package com.elysium.reddot.ms.user.domain.service;

import com.elysium.reddot.ms.user.domain.exception.type.BadValueException;
import com.elysium.reddot.ms.user.domain.model.UserModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDomainServiceImplTest {

    private static UserDomainServiceImpl userDomainService;

    @BeforeAll
    static void setUp() {
        userDomainService = new UserDomainServiceImpl();
    }

    @Test
    @DisplayName("given topicModel with null name= when validateTopicForCreation is called then throws FieldEmptyException")
    void givenTopicModelWithNullName_whenValidateBuildTopic_thenThrowsFieldEmptyException() {
        // given
        UserModel userModel = new UserModel("1L", null, "test", false, "");

        // then && throw
        assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
    }

}
