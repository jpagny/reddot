package com.elysium.reddot.ms.thread.domain.service;

import com.elysium.reddot.ms.thread.domain.exception.type.DifferentUserException;
import com.elysium.reddot.ms.thread.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.thread.domain.exception.type.FieldWithSpaceException;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;

/**
 * The ThreadDomainServiceImpl class implements the IThreadDomainService interface
 * and provides the implementation for validating and updating thread models in the domain layer.
 */
public class ThreadDomainServiceImpl implements IThreadDomainService {

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateThreadForCreation(ThreadModel threadModel) {
        validateName(threadModel.getName());
        validateLabel(threadModel.getLabel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateThreadForUpdate(ThreadModel threadModel) {
        validateLabel(threadModel.getLabel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThreadModel updateExistingThreadWithUpdates(ThreadModel threadUpdates, ThreadModel existingThread) {
        validateUser(threadUpdates.getUserId(), existingThread.getUserId());
        validateThreadForUpdate(threadUpdates);

        existingThread.setLabel(threadUpdates.getLabel());
        existingThread.setDescription(threadUpdates.getDescription());

        return existingThread;
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            throw new FieldEmptyException("name");
        }
        if (containsSpace(name)) {
            throw new FieldWithSpaceException("name");
        }

    }

    private void validateLabel(String label) {
        if (isBlank(label)) {
            throw new FieldEmptyException("label");
        }
    }

    private void validateUser(String userFromMessageToCreate, String userFromMessageExisting) {
        if (isBlank(userFromMessageToCreate)) {
            throw new FieldEmptyException("userId from message to create");
        }

        if (isBlank(userFromMessageExisting)) {
            throw new FieldEmptyException("userId from message existing");
        }

        if (!userFromMessageToCreate.equals(userFromMessageExisting)) {
            throw new DifferentUserException(userFromMessageToCreate, userFromMessageExisting);
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean containsSpace(String str) {
        return str.contains(" ");
    }
}
