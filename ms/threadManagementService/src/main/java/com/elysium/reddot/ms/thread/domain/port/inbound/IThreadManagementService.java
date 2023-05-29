package com.elysium.reddot.ms.thread.domain.port.inbound;

import com.elysium.reddot.ms.thread.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;

import java.util.List;

/**
 * The IThreadManagementService interface provides methods for managing threads in a message board system.
 */
public interface IThreadManagementService {

    /**
     * Retrieves a thread by its unique identifier.
     *
     * @param id the unique identifier of the thread.
     * @return the ThreadModel object representing the thread.
     */
    ThreadModel getThreadById(Long id);

    /**
     * Retrieves all threads.
     *
     * @return a list of ThreadModel objects representing all threads.
     */
    List<ThreadModel> getAllThreads();

    /**
     * Creates a new thread.
     *
     * @param threadModel the ThreadModel object representing the thread to create.
     * @return the ThreadModel object representing the created thread.
     * @throws FieldEmptyException if any required field in the threadModel is empty.
     */
    ThreadModel createThread(ThreadModel threadModel) throws FieldEmptyException;

    /**
     * Updates an existing thread.
     *
     * @param id the unique identifier of the thread to update.
     * @param threadModel the ThreadModel object representing the updated thread.
     * @return the ThreadModel object representing the updated thread.
     */
    ThreadModel updateThread(Long id, ThreadModel threadModel);


}
