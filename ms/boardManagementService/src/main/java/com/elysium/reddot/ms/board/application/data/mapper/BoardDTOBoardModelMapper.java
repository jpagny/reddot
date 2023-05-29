package com.elysium.reddot.ms.board.application.data.mapper;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.domain.model.BoardModel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class that provides methods for converting between BoardDTO and BoardModel objects.
 * This class should not be instantiated.
 */
public class BoardDTOBoardModelMapper {

    private BoardDTOBoardModelMapper() {
    }

    /**
     * Converts a BoardModel object into a BoardDTO object.
     *
     * @param boardModel the BoardModel to be converted
     * @return the converted BoardDTO
     */
    public static BoardDTO toDTO(BoardModel boardModel) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardModel.getId());
        boardDTO.setLabel(boardModel.getLabel());
        boardDTO.setName(boardModel.getName());
        boardDTO.setDescription(boardModel.getDescription());
        boardDTO.setTopicId(boardModel.getTopicId());
        return boardDTO;
    }

    /**
     * Converts a list of BoardModel objects to a list of BoardDTO objects.
     *
     * @param boardModels the list of BoardModel objects to convert
     * @return the list of converted BoardDTO objects
     */
    public static List<BoardDTO> toDTOList(List<BoardModel> boardModels) {
        return boardModels.stream()
                .map(BoardDTOBoardModelMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a BoardDTO object into a BoardModel object.
     *
     * @param boardDTO the BoardDTO to be converted
     * @return the converted BoardModel
     */
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
