package com.elysium.reddot.ms.board.application.port.out;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;

import java.util.List;
import java.util.Optional;


public interface IBoardRepositoryOutbound {
    BoardDTO createBoard(BoardDTO boardDTO);

    Optional<BoardDTO> findBoardById(Long id);

    Optional<BoardDTO> findBoardByName(String name);

    List<BoardDTO> findAllBoards();

    BoardDTO updateBoard(BoardDTO boardDTO);

    void deleteBoard(Long id);
}
