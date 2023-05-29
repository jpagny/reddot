package com.elysium.reddot.ms.message.domain.port.inbound;

import com.elysium.reddot.ms.message.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.message.domain.model.MessageModel;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * This interface represents the contract for a service providing CRUD operations on the {@link MessageModel} class.
 */
public interface IMessageManagementService {

    /**
     * Retrieves a specific {@link MessageModel} by its unique identifier.
     *
     * @param id the unique identifier of the message to retrieve
     * @return the {@link MessageModel} with the specified id
     */
    @ApiOperation(value = "Get a message by its id", response = MessageModel.class)
    MessageModel getMessageById(Long id);

    /**
     * Retrieves all {@link MessageModel} instances stored in the system.
     *
     * @return a list of all {@link MessageModel} instances
     */
    @ApiOperation(value = "View a list of available messages", response = List.class)
    List<MessageModel> getAllMessages();

    /**
     * Creates a new {@link MessageModel} instance in the system.
     *
     * @param messageModel the {@link MessageModel} instance to create
     * @return the created {@link MessageModel} instance
     * @throws FieldEmptyException if any required field of the messageModel is empty
     */
    @ApiOperation(value = "Add a message", response = MessageModel.class)
    MessageModel createMessage(MessageModel messageModel) throws FieldEmptyException;

    /**
     * Updates a specific {@link MessageModel} instance identified by its unique id.
     *
     * @param id the unique identifier of the {@link MessageModel} to update
     * @param messageModel the {@link MessageModel} instance containing the new data
     * @return the updated {@link MessageModel} instance
     */
    @ApiOperation(value = "Update an existing message", response = MessageModel.class)
    MessageModel updateMessage(Long id, MessageModel messageModel);

}
