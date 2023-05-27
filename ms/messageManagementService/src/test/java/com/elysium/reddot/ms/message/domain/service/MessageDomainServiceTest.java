package com.elysium.reddot.ms.message.domain.service;

import com.elysium.reddot.ms.message.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        assertThrows(FieldEmptyException.class, () -> messageDomainService.validateTopicForCreation(messageModel));
    }

    @Test
    @DisplayName("given a blank content when updating a message then an exception is thrown")
    void givenBlankContent_whenUpdatingMessage_thenExceptionIsThrown() {
        // given
        MessageModel messageModel = new MessageModel();
        messageModel.setContent("");

        // when && throw
        assertThrows(FieldEmptyException.class, () -> messageDomainService.validateTopicForUpdate(messageModel,null));
    }


}
