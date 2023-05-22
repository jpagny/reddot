package com.elysium.reddot.ms.replymessage.domain.service;

import com.elysium.reddot.ms.replymessage.domain.exception.LimitExceededException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReplyMessageDomainServiceTest {

    private static ReplyMessageDomainServiceImpl replyMessageDomainService;

    @BeforeAll
    static void setUp() {
        replyMessageDomainService = new ReplyMessageDomainServiceImpl();
    }


    @Test
    @DisplayName("given countRepliesMessage is less than maxNestedReplies when verifyNestedRepliesLimit then no exception is thrown")
    public void givenCountRepliesMessageIsLessThanMaxNestedReplies_whenVerifyNestedRepliesLimit_thenNoExceptionIsThrown() {
        // given
        int countRepliesMessage = 5;
        int maxNestedReplies = 10;

        // when & then
        assertDoesNotThrow(() -> replyMessageDomainService.verifyNestedRepliesLimit(countRepliesMessage, maxNestedReplies));
    }

    @Test
    @DisplayName("given countRepliesMessage is more than maxNestedReplies when verifyNestedRepliesLimit then throws LimitExceededException")
    public void givenCountRepliesMessageIsMoreThanMaxNestedReplies_whenVerifyNestedRepliesLimit_thenThrowsLimitExceededException() {
        // given
        int countRepliesMessage = 11;
        int maxNestedReplies = 10;

        // when & then
        assertThrows(LimitExceededException.class, () -> replyMessageDomainService.verifyNestedRepliesLimit(countRepliesMessage, maxNestedReplies));
    }

}
