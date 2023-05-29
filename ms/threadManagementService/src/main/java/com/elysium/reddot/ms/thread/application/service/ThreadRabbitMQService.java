package com.elysium.reddot.ms.thread.application.service;

import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.ThreadRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The ThreadRabbitMQService class provides methods to interact with RabbitMQ related to thread operations.
 */
@Service
@RequiredArgsConstructor
public class ThreadRabbitMQService {

    private final ThreadRepositoryAdapter threadRepositoryAdapter;

    /**
     * Checks if a thread with the given ID exists.
     *
     * @param id the ID of the thread to check.
     * @return true if the thread exists, false otherwise.
     */
    public boolean checkThreadIdExists(Long id) {
        Optional<ThreadModel> threadModel = threadRepositoryAdapter.findThreadById(id);
        return threadModel.isPresent();
    }


}
