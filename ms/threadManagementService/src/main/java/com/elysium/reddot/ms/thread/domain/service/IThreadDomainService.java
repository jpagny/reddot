package com.elysium.reddot.ms.thread.domain.service;

import com.elysium.reddot.ms.thread.domain.model.ThreadModel;

public interface IThreadDomainService {

    void validateThreadForCreation(ThreadModel thread);

    void validateThreadForUpdate(ThreadModel thread);

    ThreadModel updateExistingThreadWithUpdates(ThreadModel existingThread, ThreadModel threadToUpdate);

}
