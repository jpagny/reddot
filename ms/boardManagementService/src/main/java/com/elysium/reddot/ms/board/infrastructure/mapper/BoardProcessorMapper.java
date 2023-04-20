package com.elysium.reddot.ms.board.infrastructure.mapper;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.domain.model.BoardModel;

import java.util.List;
import java.util.stream.Collectors;

public class BoardProcessorMapper {

    private BoardProcessorMapper() {
    }

    public static BoardDTO toDTO(BoardModel boardModel) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardModel.getId());
        boardDTO.setLabel(boardModel.getLabel());
        boardDTO.setName(boardModel.getName());
        boardDTO.setDescription(boardModel.getDescription());
        return boardDTO;
    }

    public static List<BoardDTO> toDTOList(List<BoardModel> boardModels) {
        return boardModels.stream()
                .map(BoardProcessorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static BoardModel toModel(BoardDTO boardDTO) {
        return new BoardModel(
                boardDTO.getId(),
                boardDTO.getName(),
                boardDTO.getLabel(),
                boardDTO.getDescription()
        );
    }

}
