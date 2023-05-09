package com.elysium.reddot.ms.thread.infrastructure.outbound.persistence;


import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.domain.port.outbound.IThreadRepository;
import com.elysium.reddot.ms.thread.infrastructure.mapper.ThreadPersistenceMapper;
import com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.entity.ThreadJpaEntity;
import com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.repository.ThreadJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ThreadRepositoryAdapter implements IThreadRepository {

    private final ThreadJpaRepository threadJpaRepository;

    public ThreadRepositoryAdapter(ThreadJpaRepository threadJpaRepository) {
        this.threadJpaRepository = threadJpaRepository;
    }

    @Override
    public ThreadModel createThread(ThreadModel threadModel) {
        ThreadJpaEntity threadEntity = ThreadPersistenceMapper.toEntity(threadModel);
        ThreadJpaEntity savedThread = threadJpaRepository.save(threadEntity);
        return ThreadPersistenceMapper.toModel(savedThread);
    }

    @Override
    public Optional<ThreadModel> findThreadById(Long id) {
        return threadJpaRepository.findById(id)
                .map(ThreadPersistenceMapper::toModel);
    }

    @Override
    public Optional<ThreadModel> findThreadByName(String name) {
        return threadJpaRepository.findByName(name)
                .map(ThreadPersistenceMapper::toModel);
    }

    @Override
    public List<ThreadModel> findAllThreads() {
        return threadJpaRepository.findAll()
                .stream()
                .map(ThreadPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public ThreadModel updateThread(ThreadModel threadModel) {
        ThreadJpaEntity threadEntity = ThreadPersistenceMapper.toEntity(threadModel);
        ThreadJpaEntity updatedThread = threadJpaRepository.save(threadEntity);
        return ThreadPersistenceMapper.toModel(updatedThread);
    }

    @Override
    public void deleteThread(Long id) {
        threadJpaRepository.deleteById(id);
    }
}
