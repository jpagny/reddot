package com.elysium.reddot.ms.replymessage.domain.service;

import com.elysium.reddot.ms.replymessage.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.replymessage.domain.exception.LimitExceededException;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReplyMessageDomainServiceTest {

    private static ReplyMessageDomainServiceImpl replyMessageDomainService;

    @BeforeAll
    static void setUp() {
        replyMessageDomainService = new ReplyMessageDomainServiceImpl();
    }

    @Test
    @DisplayName("given a blank content when creating a reply message then an exception is thrown")
    void givenBlankContent_whenCreatingReplyMessage_thenExceptionIsThrown() {
        // given
        ReplyMessageModel messageModel = new ReplyMessageModel();
        messageModel.setContent("");

        // when && throw
        assertThrows(FieldEmptyException.class, () -> replyMessageDomainService.validateReplyMessageForCreation(messageModel));
    }

    void givenBlankContentOrEmptyUserId_whenUpdatingReplyMessage_thenFieldEmptyExceptionIsThrown(String content, Long parentMessageId, String userId) {
        // given
        ReplyMessageModel messageModel = new ReplyMessageModel(content, parentMessageId, userId);
        ReplyMessageModel messageModelExisting = new ReplyMessageModel("content", 1L, "userId");

        // when && throw
        assertThrows(FieldEmptyException.class, () -> replyMessageDomainService.validateReplyMessageForUpdate(messageModel, messageModelExisting));
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
    @DisplayName("given countRepliesMessage is less than maxNestedReplies when verifyNestedRepliesLimit then no exception is thrown")
    void givenCountRepliesMessageIsLessThanMaxNestedReplies_whenVerifyNestedRepliesLimit_thenNoExceptionIsThrown() {
        // given
        int countRepliesMessage = 5;
        int maxNestedReplies = 10;

        // when & then
        assertDoesNotThrow(() -> replyMessageDomainService.verifyNestedRepliesLimit(countRepliesMessage, maxNestedReplies));
    }

    @Test
    @DisplayName("given countRepliesMessage is more than maxNestedReplies when verifyNestedRepliesLimit then throws LimitExceededException")
    void givenCountRepliesMessageIsMoreThanMaxNestedReplies_whenVerifyNestedRepliesLimit_thenThrowsLimitExceededException() {
        // given
        int countRepliesMessage = 11;
        int maxNestedReplies = 10;

        // when & then
        assertThrows(LimitExceededException.class, () -> replyMessageDomainService.verifyNestedRepliesLimit(countRepliesMessage, maxNestedReplies));
    }

}
