package com.elysium.reddot.ms.user.domain.service;

import com.elysium.reddot.ms.user.domain.exception.type.BadValueException;
import com.elysium.reddot.ms.user.domain.model.UserModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class UserDomainServiceImplTest {

    private static UserDomainServiceImpl userDomainService;

    @BeforeAll
    static void setUp() {
        userDomainService = new UserDomainServiceImpl();
    }

    @Test
    @DisplayName("given username null value when validateUserForCreation is called then throws BadValueException")
    void givenUsernameNullValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
        // given
        UserModel userModel = new UserModel("1L", null, "e@mail", false, "");
        String expectedMessage = "Bad value in username : is required and cannot be empty";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given username empty value when validateUserForCreation is called then throws BadValueException")
    void givenUsernameEmptyValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
        // given
        UserModel userModel = new UserModel("1L", "", "e@mail", false, "");
        String expectedMessage = "Bad value in username : is required and cannot be empty";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given username blank value when validateUserForCreation is called then throws BadValueException")
    void givenUsernameBlankValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
        // given
        UserModel userModel = new UserModel("1L", "  ", "e@mail", false, "");
        String expectedMessage = "Bad value in username : is required and cannot be empty";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given email null value when validateUserForCreation is called then throws BadValueException")
    void givenEmailNullValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
        // given
        UserModel userModel = new UserModel("1L", "username", null, false, "");
        String expectedMessage = "Bad value in email : is required and cannot be empty";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given email empty value when validateUserForCreation is called then throws BadValueException")
    void givenEmailEmptyValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
        // given
        UserModel userModel = new UserModel("1L", "username", "", false, "");
        String expectedMessage = "Bad value in email : is required and cannot be empty";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given email blank value when validateUserForCreation is called then throws BadValueException")
    void givenEmailBlankValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
        // given
        UserModel userModel = new UserModel("1L", "username", " ", false, "");
        String expectedMessage = "Bad value in email : is required and cannot be empty";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given email without '@', when validating user for creation, then throw BadValueException")
    void givenEmailWithoutAtSign_whenValidatingUserForCreation_thenThrowBadValueException() {
        // given
        UserModel userModel = new UserModel("1L", "username", "emailgmail", false, "");
        String expectedMessage = "Bad value in email : must have exactly one '@' sign";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given email with multiple '@', when validating user for creation, then throw BadValueException")
    void givenEmailWithMultipleAtSign_whenValidatingUserForCreation_thenThrowBadValueException() {
        // given
        UserModel userModel = new UserModel("1L", "username", "email@g@mail", false, "");
        String expectedMessage = "Bad value in email : must have exactly one '@' sign";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given password with length less than minimum, when validating, then throw BadValueException with specific message")
    void givenPasswordWithLengthLessThanMinimum_whenValidating_thenThrowBadValueExceptionWithMessage() {
        // given
        UserModel userModel = new UserModel("1L", "username", "email@gmail", false, "pa");
        String expectedMessage = "Bad value in password : must have a minimum length 8. Password has 2";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given password without uppercase letter, when validating, then throw BadValueException with specific message")
    void givenPasswordWithoutUppercaseLetter_whenValidating_thenThrowBadValueExceptionWithMessage() {
        // given
        UserModel userModel = new UserModel("1L", "username", "email@gmail", false, "passssssss");
        String expectedMessage = "Bad value in password : must have a least one uppercase letter";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given password without digit, when validating, then throw BadValueException with specific message")
    void givenPasswordWithoutDigit_whenValidating_thenThrowBadValueExceptionWithMessage() {
        // given
        UserModel userModel = new UserModel("1L", "username", "email@gmail", false, "Passssssss");
        String expectedMessage = "Bad value in password : must have a least one digit";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given password without special character, when validating, then throw BadValueException with specific message")
    void givenPasswordWithoutSpecialCharacter_whenValidating_thenThrowBadValueExceptionWithMessage() {
        // given
        UserModel userModel = new UserModel("1L", "username", "email@gmail", false, "Pa6ssssssss");
        String expectedMessage = "Bad value in password : must have a least one special character";

        // when
        Exception exception = assertThrows(BadValueException.class, () -> userDomainService.validateUserForCreation(userModel));
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("given password with at least one special character, when validating, then no exception")
    void givenPasswordWithSpecialCharacter_whenValidating_thenNoException() {
        // given
        UserModel userModel = new UserModel("1L", "username", "email@gmail", false, "Pa6ssssssss&");

        // when && then
        assertDoesNotThrow(() -> userDomainService.validateUserForCreation(userModel));
    }

    @Test
    @DisplayName("test hash code")
    void testHashCode() {
        UserModel userModel1 = new UserModel("xxx", "username", "email", true,"password");
        UserModel userModel2 = new UserModel("xxx", "username", "email", true,"password");
        UserModel userModel3 = new UserModel("xxx2", "username3", "email3", true,"password3");
        UserModel userModel4 = new UserModel(null, "username4", "email4", true,"password4");

        assertEquals(userModel1.hashCode(), userModel2.hashCode());
        assertNotEquals(userModel1.hashCode(), userModel3.hashCode());
        assertEquals(0, userModel4.hashCode());
    }

    @Test
    @DisplayName("test topic equal")
    void testAreTopicsEqual() {
        UserModel userModel1 = new UserModel("xxx1", "username1", "email1", true,"password");
        UserModel userModel2 = new UserModel("xxx1", "username1", "email1", true,"password");
        UserModel userModel3 = new UserModel("xxx3", "username3", "email3", true,"password");
        Object otherObject = new Object();

        assertEquals(userModel1, userModel1);
        assertEquals(userModel1, userModel2);
        assertNotEquals(userModel1, userModel3);
        assertNotEquals(userModel1, otherObject);
        assertNotEquals(null, userModel1);
    }

    @Test
    @DisplayName("test to string")
    void testToString() {
        UserModel userModel1 = new UserModel("xxx1", "username1", "email1", true,"password");

        String expectedToString = "UserModel{" +
                "id=xxx1" +
                ", username='username1'" +
                ", email='email1'" +
                ", emailVerified='true'" +
                '}';

        assertEquals(expectedToString, userModel1.toString());
    }



}
