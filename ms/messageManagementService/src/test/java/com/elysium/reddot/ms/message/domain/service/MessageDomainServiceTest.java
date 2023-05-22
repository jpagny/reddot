package com.elysium.reddot.ms.message.domain.service;

import com.elysium.reddot.ms.message.domain.model.MessageModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MessageDomainServiceTest {

    private static MessageDomainServiceImpl messageDomainService;

    @BeforeAll
    static void setUp() {
        messageDomainService = new MessageDomainServiceImpl();
    }

    @Test
    @DisplayName("given a valid count of replies message when checking nested replies limit, then it should return true")
    void givenValidCountRepliesMessage_whenCheckNestedRepliesLimit_thenTrue() {
        // given
        int countRepliesMessage = 5;

        // when
        boolean result = messageDomainService.checkNestedRepliesLimit(countRepliesMessage);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("given an invalid count of replies message when checking nested replies limit, then it should return false")
    void givenInvalidCountRepliesMessage_whenCheckNestedRepliesLimit_thenFalse() {
        // given
        int countRepliesMessage = 15;

        // when
        boolean result = messageDomainService.checkNestedRepliesLimit(countRepliesMessage);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("Given a valid count of replies message equal to the custom max replies limit when checking nested replies limit, then it should return true")
    void givenCountEqualToCustomMaxReplies_whenCheckNestedRepliesLimit_thenTrue() {
        // given
        int countRepliesMessage = 5;
        int newMaxRepliesMessage = 5;

        // when
        boolean result = messageDomainService.checkNestedRepliesLimit(countRepliesMessage, newMaxRepliesMessage);

        // then
        assertTrue(result);
    }
    
}
