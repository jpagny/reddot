package com.elysium.reddot.ms.replymessage.domain.port.inbound;

import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * Service interface for managing reply messages.
 */
public interface IReplyMessageManagementService {

    /**
     * Retrieves a reply message by its ID.
     *
     * @param id the unique identifier of the reply message to be retrieved
     * @return the reply message with the given ID, or null if no such message exists
     */
    @ApiOperation(value = "Get a reply message by its ID")
    ReplyMessageModel getReplyMessageById(Long id);

    /**
     * Retrieves all reply messages.
     *
     * @return a list of all reply messages
     */
    @ApiOperation(value = "Get all reply messages")
    List<ReplyMessageModel> getAllRepliesMessage();

    /**
     * Creates a new reply message.
     *
     * @param replyMessageModel the data of the reply message to be created
     * @return the newly created reply message
     */
    @ApiOperation(value = "Create a new reply message")
    ReplyMessageModel createReplyMessage(ReplyMessageModel replyMessageModel);

    /**
     * Updates a reply message with the given ID.
     *
     * @param id the unique identifier of the reply message to be updated
     * @param replyMessageModel the new data for the reply message
     * @return the updated reply message
     */
    @ApiOperation(value = "Update a reply message with the given ID")
    ReplyMessageModel updateReplyMessage(Long id, ReplyMessageModel replyMessageModel);

}
