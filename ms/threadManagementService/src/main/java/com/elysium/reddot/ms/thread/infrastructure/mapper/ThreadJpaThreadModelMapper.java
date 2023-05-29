package com.elysium.reddot.ms.thread.infrastructure.mapper;

import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.entity.ThreadJpaEntity;

/**
 * The ThreadJpaThreadModelMapper class provides static methods to map between ThreadModel and ThreadJpaEntity objects.
 * It converts a ThreadModel object to a ThreadJpaEntity object and vice versa.
 */
public class ThreadJpaThreadModelMapper {

    private ThreadJpaThreadModelMapper() {
    }

    /**
     * Converts a ThreadModel object to a ThreadJpaEntity object.
     *
     * @param threadModel The ThreadModel object to be converted.
     * @return The corresponding ThreadJpaEntity object.
     */
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

    /**
     * Converts a ThreadJpaEntity object to a ThreadModel object.
     *
     * @param threadJpaEntity The ThreadJpaEntity object to be converted.
     * @return The corresponding ThreadModel object.
     */
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
