package com.elysium.reddot.ms.thread.application.service;

import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.ThreadRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThreadRabbitMQService {

    private final ThreadRepositoryAdapter threadRepositoryAdapter;

    public boolean checkThreadIdExists(Long id) {
        Optional<ThreadModel> threadModel = threadRepositoryAdapter.findThreadById(id);
        return threadModel.isPresent();
    }


}
