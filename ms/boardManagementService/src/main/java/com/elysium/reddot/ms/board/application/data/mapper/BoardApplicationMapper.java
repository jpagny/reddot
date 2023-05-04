package com.elysium.reddot.ms.board.application.data.mapper;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.domain.model.BoardModel;

public class BoardApplicationMapper {

    private BoardApplicationMapper() {
    }

    public static BoardDTO toDTO(BoardModel boardModel) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardModel.getId());
        boardDTO.setLabel(boardModel.getLabel());
        boardDTO.setName(boardModel.getName());
        boardDTO.setDescription(boardModel.getDescription());
        boardDTO.setTopicId(boardModel.getTopicId());
        return boardDTO;
    }

    public static BoardModel toModel(BoardDTO boardDTO) {
        return new BoardModel(
                boardDTO.getId(),
                boardDTO.getName(),
                boardDTO.getLabel(),
                boardDTO.getDescription(),
                boardDTO.getTopicId()
        );
    }

}
