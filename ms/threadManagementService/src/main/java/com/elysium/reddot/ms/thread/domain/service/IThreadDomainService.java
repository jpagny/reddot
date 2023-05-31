package com.elysium.reddot.ms.thread.domain.service;

import com.elysium.reddot.ms.thread.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;

/**
 * The IThreadDomainService interface defines methods for validating and updating thread models in the domain layer.
 */
public interface IThreadDomainService {

    /**
     * Validates a thread model for creation.
     *
     * @param thread the ThreadModel object to validate.
     *
     * @throws FieldEmptyException if any required field in the thread model is empty.
     */
    void validateThreadForCreation(ThreadModel thread);

    /**
     * Validates a thread model for update.
     *
     * @param thread the ThreadModel object to validate.
     *
     * @throws FieldEmptyException if any required field in the thread model is empty.
     */
    void validateThreadForUpdate(ThreadModel thread);

    /**
     * Updates an existing thread model with the updates from another thread model.
     *
     * @param existingThread the existing ThreadModel object to update.
     * @param threadToUpdate the ThreadModel object containing the updates.
     *
     * @return the updated ThreadModel object.
     */
    ThreadModel updateExistingThreadWithUpdates(ThreadModel existingThread, ThreadModel threadToUpdate);

}
