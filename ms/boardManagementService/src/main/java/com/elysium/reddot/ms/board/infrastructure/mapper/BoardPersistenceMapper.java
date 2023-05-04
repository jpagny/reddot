package com.elysium.reddot.ms.board.infrastructure.mapper;

import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.entity.BoardJpaEntity;

public class BoardPersistenceMapper {

    private BoardPersistenceMapper() {
    }

    public static BoardJpaEntity toEntity(BoardModel boardModel) {
        BoardJpaEntity boardJpaEntity = new BoardJpaEntity();
        boardJpaEntity.setId(boardModel.getId());
        boardJpaEntity.setName(boardModel.getName());
        boardJpaEntity.setLabel(boardModel.getLabel());
        boardJpaEntity.setDescription(boardModel.getDescription());
        boardJpaEntity.setTopicId(boardModel.getTopicId());
        return boardJpaEntity;
    }

    public static BoardModel toModel(BoardJpaEntity boardJpaEntity) {
        return new BoardModel(
                boardJpaEntity.getId(),
                boardJpaEntity.getName(),
                boardJpaEntity.getLabel(),
                boardJpaEntity.getDescription(),
                boardJpaEntity.getTopicId()
        );
    }

}
