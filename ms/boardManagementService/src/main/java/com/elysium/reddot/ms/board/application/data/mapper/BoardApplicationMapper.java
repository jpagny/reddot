package com.elysium.reddot.ms.board.application.data.mapper;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.domain.model.BoardModel;

public class BoardApplicationMapper {

    private BoardApplicationMapper() {
    }

    public static BoardDTO toDTO(BoardModel boardModel) {
        BoardDTO topicDTO = new BoardDTO();
        topicDTO.setId(boardModel.getId());
        topicDTO.setLabel(boardModel.getLabel());
        topicDTO.setName(boardModel.getName());
        topicDTO.setDescription(boardModel.getDescription());
        return topicDTO;
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
