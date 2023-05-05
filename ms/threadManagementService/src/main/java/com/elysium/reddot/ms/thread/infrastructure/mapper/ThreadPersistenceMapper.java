package com.elysium.reddot.ms.thread.infrastructure.mapper;

import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.entity.ThreadJpaEntity;

public class ThreadPersistenceMapper {

    private ThreadPersistenceMapper() {
    }

    public static ThreadJpaEntity toEntity(ThreadModel threadModel) {
        ThreadJpaEntity threadJpaEntity = new ThreadJpaEntity();
        threadJpaEntity.setId(threadModel.getId());
        threadJpaEntity.setName(threadModel.getName());
        threadJpaEntity.setLabel(threadModel.getLabel());
        threadJpaEntity.setDescription(threadModel.getDescription());
        threadJpaEntity.setBoardId(threadModel.getBoardId());
        threadJpaEntity.setUserId(threadModel.getUserId());
        return threadJpaEntity;
    }

    public static ThreadModel toModel(ThreadJpaEntity threadJpaEntity) {
        return new ThreadModel(
                threadJpaEntity.getId(),
                threadJpaEntity.getName(),
                threadJpaEntity.getLabel(),
                threadJpaEntity.getDescription(),
                threadJpaEntity.getBoardId(),
                threadJpaEntity.getUserId()
        );
    }

}
