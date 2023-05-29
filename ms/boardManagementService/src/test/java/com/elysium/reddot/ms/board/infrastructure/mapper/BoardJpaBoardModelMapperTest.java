package com.elysium.reddot.ms.board.infrastructure.mapper;

import com.elysium.reddot.ms.board.application.data.mapper.BoardJpaBoardModelMapper;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.entity.BoardJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardJpaBoardModelMapperTest {

    @Test
    @DisplayName("given boardModel when toEntity is called then returns boardJpaEntity")
    void givenBoardModel_whenToEntity_thenBoardJpaEntity() {
        // given
        BoardModel boardModel = new BoardModel(1L, "Test Name", "Test Label", "Test Description",1L);

        // when
        BoardJpaEntity boardJpaEntity = BoardJpaBoardModelMapper.toEntity(boardModel);

        // then
        assertEquals(boardModel.getId(), boardJpaEntity.getId(), "The board ID should match");
        assertEquals(boardModel.getName(), boardJpaEntity.getName(), "The board name should match");
        assertEquals(boardModel.getLabel(), boardJpaEntity.getLabel(), "The board label should match");
        assertEquals(boardModel.getDescription(), boardJpaEntity.getDescription(), "The board description should match");
    }

    @Test
    @DisplayName("given boardJpaEntity when toModel is called then returns boardModel")
    void givenBoardJpaEntity_whenToModel_thenBoardModel() {
        // given
        BoardJpaEntity boardJpaEntity = new BoardJpaEntity();
        boardJpaEntity.setId(1L);
        boardJpaEntity.setName("Test Name");
        boardJpaEntity.setLabel("Test Label");
        boardJpaEntity.setDescription("Test Description");

        // when
        BoardModel boardModel = BoardJpaBoardModelMapper.toModel(boardJpaEntity);

        // then
        assertEquals(boardJpaEntity.getId(), boardModel.getId(), "The board ID should match");
        assertEquals(boardJpaEntity.getName(), boardModel.getName(), "The board name should match");
        assertEquals(boardJpaEntity.getLabel(), boardModel.getLabel(), "The board label should match");
        assertEquals(boardJpaEntity.getDescription(), boardModel.getDescription(), "The board description should match");
    }


}
