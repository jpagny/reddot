package com.elysium.reddot.ms.board.infrastructure.mapper;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardProcessorMapperTest {

    @Test
    @DisplayName("given boardModel when toDTO is called then returns boardDTO")
    void givenBoardModel_whenToDTO_thenBoardDTO() {
        // Given
        BoardModel boardModel = new BoardModel(1L, "board1", "label1", "description1",1L);

        // When
        BoardDTO boardDTO = BoardProcessorMapper.toDTO(boardModel);

        // Then
        assertEquals(boardModel.getId(), boardDTO.getId());
        assertEquals(boardModel.getName(), boardDTO.getName());
        assertEquals(boardModel.getLabel(), boardDTO.getLabel());
        assertEquals(boardModel.getDescription(), boardDTO.getDescription());
    }

    @Test
    @DisplayName("given list of boardModels when toDTO is called then returns list of boardDTOs")
    void givenListBoardModels_whenToDTO_thenListBoardDTOs() {
        // Given
        BoardModel boardModel1 = new BoardModel(1L, "board1", "label1", "description1",1L);
        BoardModel boardModel2 = new BoardModel(2L, "board2", "label2", "description2",1L);
        List<BoardModel> boardModels = Arrays.asList(boardModel1, boardModel2);

        // When
        List<BoardDTO> boardDTOs = BoardProcessorMapper.toDTOList(boardModels);

        // Then
        assertEquals(boardModels.size(), boardDTOs.size());
        for (int i = 0; i < boardModels.size(); i++) {
            BoardModel boardModel = boardModels.get(i);
            BoardDTO boardDTO = boardDTOs.get(i);
            assertEquals(boardModel.getId(), boardDTO.getId());
            assertEquals(boardModel.getName(), boardDTO.getName());
            assertEquals(boardModel.getLabel(), boardDTO.getLabel());
            assertEquals(boardModel.getDescription(), boardDTO.getDescription());
        }
    }

    @Test
    @DisplayName("given boardDTO when toModel is called then returns boardModel")
    void givenBoardDTO_whenToModel_thenBoardModel() {
        // Given
        BoardDTO boardDTO = new BoardDTO(1L, "board1", "label1", "description1",1L);

        // When
        BoardModel boardModel = BoardProcessorMapper.toModel(boardDTO);

        // Then
        assertEquals(boardDTO.getId(), boardModel.getId());
        assertEquals(boardDTO.getName(), boardModel.getName());
        assertEquals(boardDTO.getLabel(), boardModel.getLabel());
        assertEquals(boardDTO.getDescription(), boardModel.getDescription());
    }
}