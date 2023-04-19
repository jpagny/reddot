package com.elysium.reddot.ms.board.infrastructure.mapper;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.entity.BoardJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardInfrastructureMapperTest {

    @Test
    @DisplayName("given boardDTO when toEntity is called then returns boardJpaEntity")
    void givenBoardDTO_whenToEntity_thenBoardJpaEntity() {
        // given
        BoardDTO boardDTO = new BoardDTO(1L, "Test Name", "Test Label", "Test Description");

        // when
        BoardJpaEntity boardJpaEntity = BoardInfrastructureMapper.toEntity(boardDTO);

        // then
        assertEquals(boardDTO.getId(), boardJpaEntity.getId(), "The board ID should match");
        assertEquals(boardDTO.getName(), boardJpaEntity.getName(), "The board name should match");
        assertEquals(boardDTO.getLabel(), boardJpaEntity.getLabel(), "The board label should match");
        assertEquals(boardDTO.getDescription(), boardJpaEntity.getDescription(), "The board description should match");
    }

    @Test
    @DisplayName("given boardJpaEntity when toDTO is called then returns boardDTO")
    void givenBoardJpaEntity_whenToDTO_thenBoardDTO() {
        // given
        BoardJpaEntity boardJpaEntity = new BoardJpaEntity();
        boardJpaEntity.setId(1L);
        boardJpaEntity.setName("Test Name");
        boardJpaEntity.setLabel("Test Label");
        boardJpaEntity.setDescription("Test Description");

        // when
        BoardDTO boardDTO = BoardInfrastructureMapper.toDTO(boardJpaEntity);

        // then
        assertEquals(boardJpaEntity.getId(), boardDTO.getId(), "The board ID should match");
        assertEquals(boardJpaEntity.getName(), boardDTO.getName(), "The board name should match");
        assertEquals(boardJpaEntity.getLabel(), boardDTO.getLabel(), "The board label should match");
        assertEquals(boardJpaEntity.getDescription(), boardDTO.getDescription(), "The board description should match");
    }


}
