package com.elysium.reddot.ms.board.domain.service;

import com.elysium.reddot.ms.board.domain.model.BoardModel;

public interface IBoardDomainService {

    void validateBoardForCreation(BoardModel boardModel);

    void validateBoardForUpdate(BoardModel boardModel);

    BoardModel updateExistingBoardWithUpdates(BoardModel existingBoardModel, BoardModel boardToUpdateModel);

}
