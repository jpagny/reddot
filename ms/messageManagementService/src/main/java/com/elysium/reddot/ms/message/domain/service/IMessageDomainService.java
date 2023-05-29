package com.elysium.reddot.ms.message.domain.service;

import com.elysium.reddot.ms.message.domain.exception.DifferentUserException;
import com.elysium.reddot.ms.message.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.message.domain.model.MessageModel;

/**
 * Interface providing a contract for services validating {@link MessageModel} instances before they are created or updated.
 */
public interface IMessageDomainService {

    /**
     * Performs necessary validation steps on a {@link MessageModel} instance before it can be created in the system.
     *
     * @param messageModel the {@link MessageModel} instance to validate
     * @throws FieldEmptyException if any required fields on the {@link MessageModel} are empty or null
     */
    void validateMessageForCreation(MessageModel messageModel);

    /**
     * Performs necessary validation steps on a {@link MessageModel} instance before it can be updated in the system.
     *
     * @param messageModel the updated {@link MessageModel} instance to validate
     * @param messageExisting the existing {@link MessageModel} instance in the system
     * @throws DifferentUserException if the user of the existing and updated messages do not match
     * @throws FieldEmptyException if any required fields on the updated {@link MessageModel} are empty or null
     */
    void validateMessageForUpdate(MessageModel messageModel, MessageModel messageExisting);


}
