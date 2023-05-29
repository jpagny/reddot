package com.elysium.reddot.ms.thread.domain.port.outbound;

import com.elysium.reddot.ms.thread.domain.model.ThreadModel;

import java.util.List;
import java.util.Optional;

/**
 * The IThreadRepository interface defines methods for accessing and managing threads in a repository.
 */
public interface IThreadRepository {

    /**
     * Creates a new thread in the repository.
     *
     * @param threadModel the ThreadModel object representing the thread to create.
     * @return the ThreadModel object representing the created thread.
     */
    ThreadModel createThread(ThreadModel threadModel);

    /**
     * Retrieves a thread from the repository by its unique identifier.
     *
     * @param id the unique identifier of the thread.
     * @return an Optional containing the ThreadModel object if found, or an empty Optional otherwise.
     */
    Optional<ThreadModel> findThreadById(Long id);

    /**
     * Retrieves the first thread entity with the specified name and boardId.
     *
     * @param name    The name of the thread.
     * @param boardId The ID of the board.
     * @return An Optional containing the matching ThreadModel, or an empty Optional if not found.
     */
    Optional<ThreadModel> findFirstByNameAndBoardId(String name, Long boardId);

    /**
     * Retrieves all threads from the repository.
     *
     * @return a list of ThreadModel objects representing all threads.
     */
    List<ThreadModel> findAllThreads();

    /**
     * Updates an existing thread in the repository.
     *
     * @param threadModel the ThreadModel object representing the updated thread.
     * @return the ThreadModel object representing the updated thread.
     */
    ThreadModel updateThread(ThreadModel threadModel);

}
