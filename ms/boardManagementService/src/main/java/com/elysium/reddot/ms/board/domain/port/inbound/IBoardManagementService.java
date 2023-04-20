package com.elysium.reddot.ms.board.domain.port.inbound;


import com.elysium.reddot.ms.board.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.board.domain.model.BoardModel;

import java.util.List;

public interface IBoardManagementService {

    BoardModel getBoardById(Long id) ;

    List<BoardModel> getAllBoards();

    BoardModel createBoard(BoardModel boardModel) throws FieldEmptyException;

    BoardModel updateBoard(Long id, BoardModel boardModel);

    BoardModel deleteBoardById(Long id) ;

}
