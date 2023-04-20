package com.elysium.reddot.ms.board.domain.port.outbound;

import com.elysium.reddot.ms.board.domain.model.BoardModel;

import java.util.List;
import java.util.Optional;


public interface IBoardRepository {
    BoardModel createBoard(BoardModel boardModel);

    Optional<BoardModel> findBoardById(Long id);

    Optional<BoardModel> findBoardByName(String name);

    List<BoardModel> findAllBoards();

    BoardModel updateBoard(BoardModel boardModel);

    void deleteBoard(Long id);
}
