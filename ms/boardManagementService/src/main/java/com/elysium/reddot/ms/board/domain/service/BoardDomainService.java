package com.elysium.reddot.ms.board.domain.service;

import com.elysium.reddot.ms.board.application.port.in.IBoardManagement;
import com.elysium.reddot.ms.board.domain.exception.FieldEmptyException;
import com.elysium.reddot.ms.board.domain.exception.FieldWithSpaceException;
import com.elysium.reddot.ms.board.domain.model.BoardModel;


public class BoardDomainService implements IBoardManagement {

    @Override
    public void validateBoardForCreation(BoardModel boardModel) {
        validateName(boardModel.getName());
        validateLabel(boardModel.getLabel());
    }

    @Override
    public void validateBoardForUpdate(BoardModel boardModel) {
        validateLabel(boardModel.getLabel());
    }

    @Override
    public BoardModel updateExistingBoardWithUpdates(BoardModel existingBoard, BoardModel boardToUpdate) {
        validateBoardForUpdate(boardToUpdate);

        existingBoard.setLabel(boardToUpdate.getLabel());
        existingBoard.setDescription(boardToUpdate.getDescription());

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
