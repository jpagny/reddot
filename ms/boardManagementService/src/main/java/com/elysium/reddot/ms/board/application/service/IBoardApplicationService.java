package com.elysium.reddot.ms.board.application.service;


import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.board.domain.exception.FieldEmptyException;

import java.util.List;

public interface IBoardApplicationService {

    BoardDTO getBoardById(Long id) throws ResourceNotFoundException;

    List<BoardDTO> getAllBoards();

    BoardDTO createBoard(BoardDTO boardDTO) throws FieldEmptyException;

    BoardDTO updateBoard(Long id, BoardDTO boardDto);

    void deleteBoardById(Long id) throws ResourceNotFoundException;
}
