package com.elysium.reddot.ms.user.domain.service;

import com.elysium.reddot.ms.user.domain.exception.type.BadValueException;
import com.elysium.reddot.ms.user.domain.model.UserModel;

/**
 * Implementation of the User Domain Service.
 */
public class UserDomainServiceImpl implements IUserDomainService {

    private static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateUserForCreation(UserModel userModel) {
        checkUserName(userModel.getUsername());
        checkEmail(userModel.getEmail());
        checkValidPassword(userModel.getPassword());
    }

    /**
     * Checks if the given username is valid.
     *
     * @param username the username to check
     * @throws BadValueException if the username is not valid
     */
    private void checkUserName(String username) {
        isNotEmptyElseThrow("username", username);
        // another rule
    }

    /**
     * Checks if the given email is valid.
     *
     * @param email the email to check
     * @throws BadValueException if the email is not valid
     */
    private void checkEmail(String email) {
        isNotEmptyElseThrow("email", email);
        hasOneAtSignElseThrow(email);
        // another rule
    }

    /**
     * Checks if the given password is valid.
     *
     * @param password the password to check
     * @throws BadValueException if the password is not valid
     */
    private void checkValidPassword(String password) {
        hasMinimumLengthOfElseThrow(password);
        hasOneUpperCaseElseThrow(password);
        hasOneDigitElseThrow(password);
        hasOneSpecialCharacterElseThrow(password);
    }

    /**
     * Checks if the given field is not empty.
     *
     * @param field the name of the field to check
     * @param value the value of the field to check
     * @throws BadValueException if the field is empty
     */
    private void isNotEmptyElseThrow(String field, String value) {
        if (value == null || value.isEmpty() || value.isBlank()) {
            throw new BadValueException(field, "is required and cannot be empty");
        }
    }

    /**
     * Checks if the given email contains at least one '@' sign.
     *
     * @param email the email to check
     * @throws BadValueException if the email does not contain an '@' sign
     */
    private void hasOneAtSignElseThrow(String email) {
        if (!email.matches("^[^@]*@[^@]*$")) {
            throw new BadValueException("email", "must have exactly one '@' sign");
        }
    }

    /**
     * Checks if the given password has a minimum length.
     *
     * @param password the password to check
     * @throws BadValueException if the password is shorter than the minimum length
     */
    private void hasMinimumLengthOfElseThrow(String password) {
        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new BadValueException("password", "must have a minimum length "
                    + PASSWORD_MIN_LENGTH
                    + ". Password has " + password.length());
        }
    }

    /**
     * Checks if the given password contains at least one uppercase letter.
     *
     * @param password the password to check
     * @throws BadValueException if the password does not contain an uppercase letter
     */
    private void hasOneUpperCaseElseThrow(String password) {
        if (!password.matches(".*[A-Z].*")) {
            throw new BadValueException("password", "must have a least one uppercase letter");
        }
    }

    /**
     * Checks if the given password contains at least one digit.
     *
     * @param password the password to check
     * @throws BadValueException if the password does not contain a digit
     */
    private void hasOneDigitElseThrow(String password) {
        if (!password.matches(".*[0-9].*")) {
            throw new BadValueException("password", "must have a least one digit");
        }
    }

    /**
     * Checks if the given password contains at least one special character.
     *
     * @param password the password to check
     * @throws BadValueException if the password does not contain a special character
     */
    private void hasOneSpecialCharacterElseThrow(String password) {
        if (!password.matches(".*[!@#$%^&*()-+=|<>?{}\\[\\]~].*")) {
            throw new BadValueException("password", "must have a least one special character");
        }
    }

}
