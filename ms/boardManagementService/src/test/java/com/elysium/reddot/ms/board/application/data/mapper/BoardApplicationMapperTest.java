package com.elysium.reddot.ms.board.application.data.mapper;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardApplicationMapperTest {

    @Test
    @DisplayName("given boardModel when toDTO is called then returns BoardDTO")
    void givenBoardModel_whenToDTO_thenBoardDTO() {
        // given
        BoardModel boardModel = new BoardModel(1L, "Test Name", "Test Label", "Test Description");

        // when
        BoardDTO boardDTO = BoardApplicationMapper.toDTO(boardModel);

        // then
        assertEquals(boardModel.getId(), boardDTO.getId(), "The board ID should match");
        assertEquals(boardModel.getName(), boardDTO.getName(), "The board name should match");
        assertEquals(boardModel.getLabel(), boardDTO.getLabel(), "The board label should match");
        assertEquals(boardModel.getDescription(), boardDTO.getDescription(), "The board description should match");
    }

    @Test
    @DisplayName("given boardDTO when toModel is called then returns boardModel")
    void givenBoardDTO_whenToModel_thenBoardModel() {
        // given
        BoardDTO boardDTO = new BoardDTO(1L, "Test Name", "Test Label", "Test Description");

        // when
        BoardModel boardModel = BoardApplicationMapper.toModel(boardDTO);

        // then
        assertEquals(boardDTO.getId(), boardModel.getId(), "The board ID should match");
        assertEquals(boardDTO.getName(), boardModel.getName(), "The board name should match");
        assertEquals(boardDTO.getLabel(), boardModel.getLabel(), "The board label should match");
        assertEquals(boardDTO.getDescription(), boardModel.getDescription(), "The board description should match");
    }


}
