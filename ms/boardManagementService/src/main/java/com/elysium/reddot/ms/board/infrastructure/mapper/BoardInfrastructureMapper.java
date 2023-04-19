package com.elysium.reddot.ms.board.infrastructure.mapper;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.entity.BoardJpaEntity;

public class BoardInfrastructureMapper {

    private BoardInfrastructureMapper() {
    }

    public static BoardJpaEntity toEntity(BoardDTO boardDTO) {
        BoardJpaEntity boardJpaEntity = new BoardJpaEntity();
        boardJpaEntity.setId(boardDTO.getId());
        boardJpaEntity.setName(boardDTO.getName());
        boardJpaEntity.setLabel(boardDTO.getLabel());
        boardJpaEntity.setDescription(boardDTO.getDescription());
        return boardJpaEntity;
    }

    public static BoardDTO toDTO(BoardJpaEntity boardJpaEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardJpaEntity.getId());
        boardDTO.setName(boardJpaEntity.getName());
        boardDTO.setLabel(boardJpaEntity.getLabel());
        boardDTO.setDescription(boardJpaEntity.getDescription());
        return boardDTO;
    }

}
