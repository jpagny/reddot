package com.elysium.reddot.ms.board.domain.service;

import com.elysium.reddot.ms.board.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.board.domain.exception.type.FieldWithSpaceException;
import com.elysium.reddot.ms.board.domain.model.BoardModel;

/**
 * Interface for the board domain service.
 * Provides methods for validating and updating BoardModel objects.
 */
public interface IBoardDomainService {

    /**
     * Validates a BoardModel for creation.
     *
     * @param boardModel the BoardModel to be validated for creation
     * @throws FieldEmptyException if any required field in the BoardModel is empty
     * @throws FieldWithSpaceException if the name of the BoardModel contains spaces
     */
    void validateBoardForCreation(BoardModel boardModel);

    /**
     * Validates a BoardModel for updating.
     *
     * @param boardModel the BoardModel to be validated for updating
     * @throws FieldEmptyException if the label of the BoardModel is empty
     */
    void validateBoardForUpdate(BoardModel boardModel);

    /**
     * Updates an existing BoardModel with new updates.
     *
     * @param existingBoardModel the existing BoardModel to be updated
     * @param boardToUpdateModel the BoardModel containing updated information
     * @return the updated BoardModel
     */
    BoardModel updateExistingBoardWithUpdates(BoardModel existingBoardModel, BoardModel boardToUpdateModel);

}
