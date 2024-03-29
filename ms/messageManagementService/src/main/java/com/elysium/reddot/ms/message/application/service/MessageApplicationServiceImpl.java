package com.elysium.reddot.ms.message.application.service;

import com.elysium.reddot.ms.message.application.exception.type.IsNotOwnerMessageException;
import com.elysium.reddot.ms.message.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.message.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.message.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.message.domain.exception.DifferentUserException;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import com.elysium.reddot.ms.message.domain.port.inbound.IMessageManagementService;
import com.elysium.reddot.ms.message.domain.port.outbound.IMessageRepository;
import com.elysium.reddot.ms.message.domain.service.MessageDomainServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is a service class for handling the business logic related to messages. It implements the IMessageManagementService interface.
 */
@Service
@Transactional
@Slf4j
public class MessageApplicationServiceImpl implements IMessageManagementService {

    private static final String RESOURCE_NAME_MESSAGE = "message";
    private final MessageDomainServiceImpl messageDomainService;
    private final IMessageRepository messageRepository;

    @Autowired
    public MessageApplicationServiceImpl(IMessageRepository messageRepository) {
        this.messageDomainService = new MessageDomainServiceImpl();
        this.messageRepository = messageRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageModel getMessageById(Long id) {
        log.debug("Fetching message with id {}", id);

        MessageModel foundMessageModel = messageRepository.findMessageById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_MESSAGE, String.valueOf(id)));

        log.info("Successfully retrieved message with id {}, name '{}'", id, foundMessageModel.getContent());

        return foundMessageModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageModel> getAllMessages() {
        log.info("Fetching all messages from database...");

        return messageRepository.findAllMessages().parallelStream().collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageModel createMessage(MessageModel messageToCreateModel) {

        log.debug("Creating new message with content '{}'", messageToCreateModel.getContent());

        Optional<MessageModel> existingMessage = messageRepository
                .findFirstByContentAndThreadId(messageToCreateModel.getContent(), messageToCreateModel.getThreadId());

        if (existingMessage.isPresent()) {
            throw new ResourceAlreadyExistException(RESOURCE_NAME_MESSAGE, "content", messageToCreateModel.getContent(), messageToCreateModel.getThreadId());
        }

        messageToCreateModel.setCreatedAt(LocalDateTime.now());
        messageToCreateModel.setUpdatedAt(messageToCreateModel.getCreatedAt());

        try {
            messageDomainService.validateMessageForCreation(messageToCreateModel);

        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_MESSAGE, exception.getMessage());
        }

        MessageModel createdMessageModel = messageRepository.createMessage(messageToCreateModel);

        log.info("Successfully created message with id {}, content '{}'", createdMessageModel.getId(), createdMessageModel.getContent());

        return createdMessageModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageModel updateMessage(Long id, MessageModel messageToUpdateModel) {
        log.debug("Updating message with content '{}'", messageToUpdateModel.getContent());

        Optional<MessageModel> messageExisting = messageRepository.findMessageById(id);

        if (messageExisting.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NAME_MESSAGE, String.valueOf(id));
        }

        try {
            messageDomainService.validateMessageForUpdate(messageToUpdateModel, messageExisting.get());

        } catch (DifferentUserException exception) {
            log.error(exception.getMessage());
            throw new IsNotOwnerMessageException();

        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_MESSAGE, exception.getMessage());

        }

        messageToUpdateModel.setId(id);
        messageToUpdateModel.setThreadId(messageExisting.get().getThreadId());
        messageToUpdateModel.setCreatedAt(messageExisting.get().getCreatedAt());
        messageToUpdateModel.setUpdatedAt(LocalDateTime.now());

        MessageModel updatedMessageModel = messageRepository.updateMessage(messageToUpdateModel);

        log.info("Successfully updated message with id '{}', content'{}", updatedMessageModel.getId(), updatedMessageModel.getContent());

        return updatedMessageModel;
    }

}