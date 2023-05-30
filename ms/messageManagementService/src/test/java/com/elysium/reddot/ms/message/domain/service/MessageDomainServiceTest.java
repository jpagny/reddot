package com.elysium.reddot.ms.message.domain.service;

import com.elysium.reddot.ms.message.domain.exception.DifferentUserException;
import com.elysium.reddot.ms.message.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageDomainServiceTest {

    private static MessageDomainServiceImpl messageDomainService;

    @BeforeAll
    static void setUp() {
        messageDomainService = new MessageDomainServiceImpl();
    }

    @Test
    @DisplayName("given a blank content when creating a message then an exception is thrown")
    void givenBlankContent_whenCreatingMessage_thenExceptionIsThrown() {
        // given
        MessageModel messageModel = new MessageModel();
        messageModel.setContent("");

        // when && throw
        assertThrows(FieldEmptyException.class, () -> messageDomainService.validateMessageForCreation(messageModel));
    }

    void givenBlankContentOrEmptyUserId_whenUpdatingReplyMessage_thenFieldEmptyExceptionIsThrown(String content, Long threadId, String userId) {
        // given
        MessageModel messageModel = new MessageModel(content, threadId, userId);
        MessageModel messageModelExisting = new MessageModel("content", 1L, "userId");

        // when && throw
        assertThrows(FieldEmptyException.class, () -> messageDomainService.validateMessageForUpdate(messageModel, messageModelExisting));
    }

    static Stream<Arguments> blankContentOrEmptyUserIdProvider() {
        return Stream.of(
                Arguments.of("", 1L, "userId"),
                Arguments.of("content", 1L, ""),
                Arguments.of("", 1L, "")
        );
    }

    @ParameterizedTest
    @MethodSource("blankContentOrEmptyUserIdProvider")
    @DisplayName("given a blank content or empty userId when updating a reply message, then a FieldEmptyException is thrown")
    void testBlankContentOrEmptyUserId(String content, Long parentMessageId, String userId) {
        givenBlankContentOrEmptyUserId_whenUpdatingReplyMessage_thenFieldEmptyExceptionIsThrown(content, parentMessageId, userId);
    }

    @Test
    @DisplayName("given different users when updating a message then an exception is thrown")
    void givenDifferentUser_whenUpdatingMessage_thenExceptionIsThrown() {
        // given
        MessageModel messageModel = new MessageModel("content", 1L, "userId1");
        MessageModel messageModelExisting = new MessageModel("content", 1L, "userId2");


        // when && throw
        assertThrows(DifferentUserException.class, () -> messageDomainService.validateMessageForUpdate(messageModel,messageModelExisting));
    }


}
