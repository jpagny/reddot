package com.elysium.reddot.ms.user.domain.service;

import com.elysium.reddot.ms.user.domain.exception.type.BadValueException;
import com.elysium.reddot.ms.user.domain.model.UserModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class UserDomainServiceImplTest {

    private static UserDomainServiceImpl userDomainService;

    @BeforeAll
    static void setUp() {
        userDomainService = new UserDomainServiceImpl();
    }

    @Test
    @DisplayName("given username null value when validateUserForCreation is called then throws BadValueException")
    public void givenUsernameNullValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
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
    public void givenUsernameEmptyValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
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
    public void givenUsernameBlankValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
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
    public void givenEmailNullValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
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
    public void givenEmailEmptyValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
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
    public void givenEmailBlankValue_whenIsNotEmptyElseThrow_thenThrowsBadValueException() {
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
    public void givenEmailWithoutAtSign_whenValidatingUserForCreation_thenThrowBadValueException() {
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
    public void givenEmailWithMultipleAtSign_whenValidatingUserForCreation_thenThrowBadValueException() {
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
    public void givenPasswordWithLengthLessThanMinimum_whenValidating_thenThrowBadValueExceptionWithMessage() {
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
    public void givenPasswordWithoutUppercaseLetter_whenValidating_thenThrowBadValueExceptionWithMessage() {
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
    public void givenPasswordWithoutDigit_whenValidating_thenThrowBadValueExceptionWithMessage() {
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
    public void givenPasswordWithoutSpecialCharacter_whenValidating_thenThrowBadValueExceptionWithMessage() {
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
    public void givenPasswordWithSpecialCharacter_whenValidating_thenNoException() {
        // given
        UserModel userModel = new UserModel("1L", "username", "email@gmail", false, "Pa6ssssssss&");

        // when && then
        assertDoesNotThrow(() -> userDomainService.validateUserForCreation(userModel));
    }


}
