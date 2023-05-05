package com.elysium.reddot.ms.thread.domain.service;

import com.elysium.reddot.ms.thread.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.thread.domain.exception.FieldWithSpaceException;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;

public class ThreadDomainServiceImpl implements IThreadDomainService {

    @Override
    public void validateThreadForCreation(ThreadModel threadModel) {
        validateName(threadModel.getName());
        validateLabel(threadModel.getLabel());
    }

    @Override
    public void validateThreadForUpdate(ThreadModel threadModel) {
        validateLabel(threadModel.getLabel());
    }

    @Override
    public ThreadModel updateExistingThreadWithUpdates(ThreadModel existingThread, ThreadModel threadUpdates) {
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

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean containsSpace(String str) {
        return str.contains(" ");
    }
}
