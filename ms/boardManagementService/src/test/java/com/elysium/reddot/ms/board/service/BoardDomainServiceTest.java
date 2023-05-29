package com.elysium.reddot.ms.board.service;

import com.elysium.reddot.ms.board.domain.exception.type.FieldEmptyException;
import com.elysium.reddot.ms.board.domain.exception.type.FieldWithSpaceException;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.domain.service.BoardDomainServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardDomainServiceTest {

    private static BoardDomainServiceImpl boardDomainService;

    @BeforeAll
    static void setUp() {
        boardDomainService = new BoardDomainServiceImpl();
    }

    @Test
    @DisplayName("given boardModel with null name= when validateBoardForCreation is called then throws FieldEmptyException")
    void givenBoardModelWithNullName_whenValidateBuildBoard_thenThrowsFieldEmptyException() {
        // given
        BoardModel boardModel = new BoardModel(1L, null, "test", "description",1L);

        // then && throw
        assertThrows(FieldEmptyException.class, () -> boardDomainService.validateBoardForCreation(boardModel));
    }

    @Test
    @DisplayName("given boardModel with blank name when validateBoardForCreation is called then throws FieldEmptyException")
    void givenBoardModelWithBlankName_whenValidateBuildBoard_thenThrowsFieldEmptyException() {
        // given
        BoardModel boardModel = new BoardModel(1L, "", "test", "description",1L);

        // then && throw
        assertThrows(FieldEmptyException.class, () -> boardDomainService.validateBoardForCreation(boardModel));
    }

    @Test
    @DisplayName("given boardModel with space name when validateBoardForCreation is called then throws FieldWithSpaceException")
    void givenBoardModelWithSpaceName_whenValidateBuildBoard_thenThrowsFieldWithSpaceException() {
        // given
        BoardModel boardModel = new BoardModel(1L, "name 1", "test", "description",1L);

        // then && throw
        assertThrows(FieldWithSpaceException.class, () -> boardDomainService.validateBoardForCreation(boardModel));
    }

    @Test
    @DisplayName("given boardModel with null label when validateBoardForCreation is called then throws FieldEmptyException")
    void givenBoardModelWithNullLabel_whenValidateBuildBoard_thenThrowsFieldEmptyException() {
        // given
        BoardModel boardModel = new BoardModel(1L, "test", null, "description",1L);

        // then && throw
        assertThrows(FieldEmptyException.class, () -> boardDomainService.validateBoardForCreation(boardModel));
    }

    @Test
    @DisplayName("given boardModelWith blank label when validateBoardForCreation is called then throws FieldEmptyException")
    void givenBoardModelWithBlankLabel_whenValidateBuildBoard_thenThrowsFieldEmptyException() {
        // given
        BoardModel boardModel = new BoardModel(1L, "test", " ", "description",1L);

        // then && throw
        assertThrows(FieldEmptyException.class, () -> boardDomainService.validateBoardForCreation(boardModel));
    }

    @Test
    @DisplayName("given valid boardModel when validateBoardForCreation is called then no exception thrown")
    void givenValidBoardModel_whenValidateBuildBoard_thenNoExceptionThrown() {
        BoardModel boardModel = new BoardModel(1L, "test", "test", "description",1L);

        assertDoesNotThrow(() -> boardDomainService.validateBoardForCreation(boardModel),
                "validateBuildBoard should not throw an exception for a valid BoardModel");
    }

    @Test
    @DisplayName("given existing board and board to update when updateExistingBoardWithUpdates is called then board updated successfully")
    void givenExistingBoardAndBoardToUpdate_whenUpdateBoard_thenBoardUpdatedSuccessfully() {
        // given
        BoardModel existingBoard = new BoardModel(1L, "test", "test", "description",1L);
        BoardModel boardToUpdate = new BoardModel(existingBoard.getId(), existingBoard.getName(), existingBoard.getLabel(), existingBoard.getDescription(),1L);
        boardToUpdate.setLabel("Updated Label");
        boardToUpdate.setDescription("Updated Description");

        // when
        BoardModel updatedBoard = boardDomainService.updateExistingBoardWithUpdates(existingBoard, boardToUpdate);

        // then
        assertEquals("Updated Label", updatedBoard.getLabel());
        assertEquals("Updated Description", updatedBoard.getDescription());
        assertEquals("test", updatedBoard.getName());
    }

    @Test
    @DisplayName("given boardModel to update with blank label when validateBoardForCreation is called then throws FieldEmptyException")
    void givenBoardModelToUpdateWithBlankLabel_whenValidateUpdateBoard_thenThrowsFieldEmptyException() {
        // given
        BoardModel existingBoard = new BoardModel(1L, "test", "test", "description",1L);
        BoardModel boardToUpdate = new BoardModel(existingBoard.getId(), existingBoard.getName(), existingBoard.getLabel(), existingBoard.getDescription(),1L);
        boardToUpdate.setLabel("");
        boardToUpdate.setDescription("Updated Description");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> boardDomainService.validateBoardForUpdate(boardToUpdate));
    }

    @Test
    @DisplayName("test hash code")
    void testHashCode() {
        BoardModel board1 = new BoardModel(1L, "name1", "label1", "description1",1L);
        BoardModel board2 = new BoardModel(1L, "name2", "label2", "description2",1L);
        BoardModel board3 = new BoardModel(2L, "name1", "label1", "description1",1L);
        BoardModel board4 = new BoardModel(null, "name1", "label1", "description1",1L);

        assertEquals(board1.hashCode(), board2.hashCode());
        assertNotEquals(board1.hashCode(), board3.hashCode());
        assertEquals(0, board4.hashCode());
    }

    @Test
    @DisplayName("test board equal")
    void testAreBoardsEqual() {
        BoardModel board1 = new BoardModel(1L, "name1", "label1", "description1",1L);
        BoardModel board2 = new BoardModel(1L, "name2", "label2", "description2",1L);
        BoardModel board3 = new BoardModel(2L, "name3", "label3", "description3",1L);
        Object otherObject = new Object();

        assertEquals(board1, board1);
        assertEquals(board1, board2);
        assertNotEquals(board1, board3);
        assertNotEquals(board1, otherObject);
        assertNotEquals(null, board1);
    }

    @Test
    @DisplayName("test to string")
    void testToString() {
        BoardModel board = new BoardModel(1L, "name1", "label1", "description1",1L);

        String expectedToString = "Board{" +
                "id=" + 1L +
                ", name='name1'" +
                ", label='label1'" +
                ", description='description1'" +
                '}';

        assertEquals(expectedToString, board.toString());
    }

}
