package com.elysium.reddot.ms.board.domain.service;


import com.elysium.reddot.ms.board.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.board.domain.exception.type.FieldWithSpaceException;
import com.elysium.reddot.ms.board.domain.model.BoardModel;

/**
 * Implementation of the IBoardDomainService interface.
 * This class provides methods for validating and updating BoardModel objects.
 */
public class BoardDomainServiceImpl implements IBoardDomainService {

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateBoardForCreation(BoardModel boardModel) {
        validateName(boardModel.getName());
        validateLabel(boardModel.getLabel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateBoardForUpdate(BoardModel boardModel) {
        validateLabel(boardModel.getLabel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoardModel updateExistingBoardWithUpdates(BoardModel existingBoard, BoardModel boardUpdates) {
        validateBoardForUpdate(boardUpdates);

        existingBoard.setLabel(boardUpdates.getLabel());
        existingBoard.setDescription(boardUpdates.getDescription());

        return existingBoard;
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            throw new FieldEmptyException("name");
        }
        if (containsSpace(name)) {
            throw new FieldWithSpaceException("name");
        }
    }

    private void validateLabel(String label) {
        if (isBlank(label)) {
            throw new FieldEmptyException("label");
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean containsSpace(String str) {
        return str.contains(" ");
    }
}
