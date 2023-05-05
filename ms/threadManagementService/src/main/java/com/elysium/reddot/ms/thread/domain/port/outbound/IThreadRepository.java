package com.elysium.reddot.ms.thread.domain.port.outbound;

import com.elysium.reddot.ms.thread.domain.model.ThreadModel;

import java.util.List;
import java.util.Optional;

public interface IThreadRepository {

    ThreadModel createThread(ThreadModel threadModel);

    Optional<ThreadModel> findThreadById(Long id);

    Optional<ThreadModel> findThreadByName(String name);

    List<ThreadModel> findAllThreads();

    ThreadModel updateThread(ThreadModel threadModel);

    void deleteThread(Long id);
}
