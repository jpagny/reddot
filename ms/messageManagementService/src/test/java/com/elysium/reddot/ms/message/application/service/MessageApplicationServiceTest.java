package com.elysium.reddot.ms.message.application.service;

import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.infrastructure.outbound.persistence.MessageRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageApplicationServiceTest {

    @Mock
    private MessageRepositoryAdapter messageRepository;

    @InjectMocks
    private MessageApplicationServiceImpl messageApplicationService;

    @Test
    @DisplayName("Given a valid message id when getting message by id, then return the message")
    void givenValidId_whenGetMessageById_thenReturnMessage() {
        // given
        Long id = 1L;
        MessageModel expectedMessage = new MessageModel();
        expectedMessage.setId(id);
        when(messageRepository.findMessageById(id)).thenReturn(Optional.of(expectedMessage));

        // when
        MessageModel foundMessage = messageApplicationService.getMessageById(id);

        // then
        assertNotNull(foundMessage);
        assertEquals(expectedMessage, foundMessage);
    }

    @Test
    @DisplayName("given an invalid message id when getting message by id, then throw ResourceNotFoundException")
    void givenInvalidId_whenGetMessageById_thenThrowException() {
        // given
        Long id = 1L;
        when(messageRepository.findMessageById(id)).thenReturn(Optional.empty());

        // when / then
        assertThrows(ResourceNotFoundException.class, () -> messageApplicationService.getMessageById(id));
    }

    @Test
    @DisplayName("given available messages in the repository when getting all messages, then return list of all messages")
    void givenAvailableMessages_whenGetAllMessages_thenReturnListOfAllMessages() {
        // given
        MessageModel message1 = new MessageModel();
        MessageModel message2 = new MessageModel();
        when(messageRepository.findAllMessages()).thenReturn(Arrays.asList(message1, message2));

        // when
        List<MessageModel> foundMessages = messageApplicationService.getAllMessages();

        // then
        assertNotNull(foundMessages);
        assertEquals(2, foundMessages.size());
    }

    @Test
    @DisplayName("given a valid message model when creating a message, then return the created message model")
    void givenValidMessageModel_whenCreateMessage_thenReturnCreatedMessageModel() {
        // given
        MessageModel messageToCreateModel = new MessageModel("content",1L,"userId");
        MessageModel expectedMessageModel = new MessageModel("content",1L,"userId");
        expectedMessageModel.setCreatedAt(LocalDateTime.now());
        expectedMessageModel.setUpdatedAt(expectedMessageModel.getCreatedAt());
        when(messageRepository.createMessage(messageToCreateModel)).thenReturn(expectedMessageModel);

        // when
        MessageModel createdMessageModel = messageApplicationService.createMessage(messageToCreateModel);

        // then
        assertNotNull(createdMessageModel);
        assertEquals(expectedMessageModel, createdMessageModel);
    }

    @Test
    @DisplayName("given a problem during the message creation when creating a message, then throw ResourceBadValueException")
    void givenProblemDuringCreation_whenCreateMessage_thenThrowException() {
        // given
        MessageModel messageToCreateModel = new MessageModel();

        // when / then
        assertThrows(ResourceBadValueException.class, () -> messageApplicationService.createMessage(messageToCreateModel));
    }

    @Test
    @DisplayName("given valid id and message model when update message then returns updated message model")
    void givenValidIdAndMessageModel_whenUpdateMessage_thenReturnUpdatedMessageModel() {
        // given
        Long id = 1L;
        MessageModel messageModel = new MessageModel();
        messageModel.setContent("Content");
        MessageModel updatedMessageModel = new MessageModel();
        updatedMessageModel.setContent("Updated Content");

        when(messageRepository.findMessageById(id)).thenReturn(Optional.of(messageModel));
        when(messageRepository.updateMessage(any(MessageModel.class))).thenReturn(updatedMessageModel);

        // when
        MessageModel result = messageApplicationService.updateMessage(id, messageModel);

        // then
        assertEquals(updatedMessageModel, result);
    }

    @Test
    @DisplayName("given non-existent id and message model when update message then throws ResourceNotFoundException")
    void givenNonExistentIdAndMessageModel_whenUpdateMessage_thenThrowsResourceNotFoundException() {
        // given
        Long id = 1L;
        MessageModel messageModel = new MessageModel();
        messageModel.setContent("Content");

        when(messageRepository.findMessageById(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> messageApplicationService.updateMessage(id, messageModel));
    }



}
