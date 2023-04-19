package com.elysium.reddot.ms.board.application.port.in;


import com.elysium.reddot.ms.board.domain.model.BoardModel;

public interface IBoardManagement {

    void validateBoardForCreation(BoardModel boardModel);

    void validateBoardForUpdate(BoardModel boardModel);

    BoardModel updateExistingBoardWithUpdates(BoardModel existingBoard, BoardModel boardToUpdate);

}
