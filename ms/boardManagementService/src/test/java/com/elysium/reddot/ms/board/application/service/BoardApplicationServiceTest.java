package com.elysium.reddot.ms.board.application.service;

import com.elysium.reddot.ms.board.application.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.board.application.exception.ResourceBadValueException;
import com.elysium.reddot.ms.board.application.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.IBoardRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardApplicationServiceTest {

    private BoardApplicationServiceImpl boardService;
    @Mock
    private IBoardRepositoryAdapter boardRepository;

    @BeforeEach
    void setUp() {
        boardService = new BoardApplicationServiceImpl(boardRepository);
    }

    @Test
    @DisplayName("given valid id when getBoardById is called then returns board")
    void givenValidId_whenGetBoardById_thenReturnsBoard() {
        // given
        Long validId = 1L;
        BoardModel expectedBoard = new BoardModel(1L, "test", "Test Name", "Test Description",1L);

        // mock
        when(boardRepository.findBoardById(validId)).thenReturn(Optional.of(expectedBoard));

        // when
        BoardModel actualBoard = boardService.getBoardById(validId);

        // then
        assertEquals(expectedBoard, actualBoard);
        verify(boardRepository, times(1)).findBoardById(validId);
    }

    @Test
    @DisplayName("given invalid id when getBoardById is called then throws ResourceNotFoundException")
    void givenInvalidId_whenGetBoardById_thenThrowsResourceNotFoundException() {
        // given
        Long invalidId = 99L;

        // mock
        when(boardRepository.findBoardById(invalidId)).thenReturn(Optional.empty());

        // then && when
        assertThrows(ResourceNotFoundException.class,
                () -> boardService.getBoardById(invalidId),
                "getBoardById should throw a ResourceNotFoundException for an invalid ID");
        verify(boardRepository, times(1)).findBoardById(invalidId);
    }

    @Test
    @DisplayName("given existing boards, when getAll, then returns List of boards")
    void givenExistingBoards_whenGetAll_thenReturnsListOfBoards() {
        // given
        BoardModel board1Model = new BoardModel(1L, "test1", "Test Name 1", "Test Description 1",1L);
        BoardModel board2Model = new BoardModel(2L, "test2", "Test Name 2", "Test Description 2",1L);

        // mock
        List<BoardModel> expectedListBoards = Arrays.asList(board1Model, board2Model);
        when(boardService.getAllBoards()).thenReturn(expectedListBoards);

        // when
        List<BoardModel> actualBoards = boardService.getAllBoards();

        // then
        assertEquals(expectedListBoards, actualBoards, "The returned board list should match the expected board list");
        verify(boardRepository, times(1)).findAllBoards();
    }

    @Test
    @DisplayName("given valid board when createBoard is called then board created")
    void givenValidBoard_whenCreateBoard_thenBoardCreated() {
        // given
        BoardModel boardToCreateLabel = new BoardModel(null, "test", "Test Label", "Test Description",1L);
        BoardModel expectedBoard = new BoardModel(1L, "test", "Test Label", "Test Description",1L);

        // mock
        when(boardRepository.findBoardByName(boardToCreateLabel.getName())).thenReturn(Optional.empty());
        when(boardRepository.createBoard(boardToCreateLabel)).thenReturn(expectedBoard);

        // when
        BoardModel actualBoardModel = boardService.createBoard(boardToCreateLabel);

        // then
        assertEquals(expectedBoard, actualBoardModel, "The created board should match the expected board");
        verify(boardRepository, times(1)).findBoardByName(expectedBoard.getName());
        verify(boardRepository, times(1)).createBoard(boardToCreateLabel);
    }

    @Test
    @DisplayName("given existing board when createBoard is called then throws ResourceAlreadyExistException")
    void givenExistingBoard_whenCreateBoard_thenThrowsResourceAlreadyExistException() {
        // given
        BoardModel existingBoardModel = new BoardModel(1L, "Test Name", "Test Label", "Test Description",1L);

        // mock
        when(boardRepository.findBoardByName(existingBoardModel.getName())).thenReturn(Optional.of(existingBoardModel));

        // when && then
        assertThrows(ResourceAlreadyExistException.class,
                () -> boardService.createBoard(existingBoardModel),
                "createBoard should throw a ResourceAlreadyExistException for an existing board");
        verify(boardRepository, times(1)).findBoardByName(existingBoardModel.getName());
    }

    @Test
    @DisplayName("given invalid board when createBoard is called then throws ResourceBadValueException")
    void givenInvalidBoard_whenCreateBoard_thenThrowsResourceBadValueException() {
        // given
        BoardModel invalidBoardModel = new BoardModel(1L, "", "Invalid Label", "Invalid Description",1L);

        // mock
        when(boardRepository.findBoardByName(invalidBoardModel.getName())).thenReturn(Optional.empty());

        // when && then
        assertThrows(ResourceBadValueException.class,
                () -> boardService.createBoard(invalidBoardModel),
                "createBoard should throw a ResourceBadValueException for an invalid board");
        verify(boardRepository, times(1)).findBoardByName(invalidBoardModel.getName());
    }

    @Test
    @DisplayName("given validBoard when updateBoard is called then board updated")
    void givenValidBoard_whenUpdateBoard_thenBoardUpdated() {
        // given
        Long boardId = 1L;
        BoardModel existingBoardModel = new BoardModel(boardId, "Old Name", "OldLabel", "Old Description",1L);
        BoardModel boardToUpdateModel = new BoardModel(boardId, "Old Name", "NewLabel", "NewDescription",1L);
        BoardModel expectedBoard = new BoardModel(boardId, "Old Name", "NewLabel", "NewDescription",1L);

        // mock
        when(boardRepository.findBoardById(boardId)).thenReturn(Optional.of(existingBoardModel));
        when(boardRepository.updateBoard(expectedBoard)).thenReturn(expectedBoard);

        // when
        BoardModel actualBoardDTO = boardService.updateBoard(1L, boardToUpdateModel);

        // then
        assertEquals(expectedBoard, actualBoardDTO, "The updated board should match the expected board");
        verify(boardRepository, times(1)).findBoardById(boardId);
        verify(boardRepository, times(1)).updateBoard(expectedBoard);
    }

    @Test
    @DisplayName("given non-existent board when updateBoard is called then throws ResourceNotFoundException")
    void givenNonExistentBoard_whenUpdateBoard_thenThrowsResourceNotFoundException() {
        // given
        Long nonExistentBoardId = 99L;
        BoardModel boardToUpdateModel = new BoardModel(nonExistentBoardId, "New Name", "New Label", "New Description",1L);

        // mock
        when(boardRepository.findBoardById(nonExistentBoardId)).thenReturn(Optional.empty());

        // then && when
        assertThrows(ResourceNotFoundException.class,
                () -> boardService.updateBoard(nonExistentBoardId, boardToUpdateModel),
                "updateBoard should throw a ResourceNotFoundException for a non-existent board");
        verify(boardRepository, times(1)).findBoardById(nonExistentBoardId);
    }

    @Test
    @DisplayName("given invalid board when updateBoard is called then throws ResourceBadValueException")
    void givenInvalidBoard_whenUpdateBoard_thenThrowsResourceBadValueException() {
        // given
        Long boardId = 1L;
        BoardModel existingBoardModel = new BoardModel(boardId, "Old Name", "Old Label", "Old Description",1L);
        BoardModel invalidBoardToUpdateModel = new BoardModel(boardId, "Invalid Name", "", "Invalid Description",1L);

        // mock
        when(boardRepository.findBoardById(boardId)).thenReturn(Optional.of(existingBoardModel));

        // when && then
        assertThrows(ResourceBadValueException.class,
                () -> boardService.updateBoard
                        (boardId, invalidBoardToUpdateModel),
                "updateBoard should throw a ResourceBadValueException for an invalid board");
        verify(boardRepository, times(1)).findBoardById(boardId);
    }

    @Test
    @DisplayName("given existing board when deleteBoardById is called then board deleted")
    void givenExistingBoard_whenDeleteBoardById_thenBoardDeleted() {
        // given
        Long boardId = 1L;
        BoardModel existingBoardModel = new BoardModel(boardId, "Test Name", "Test Label", "Test Description",1L);

        // mock
        when(boardRepository.findBoardById(boardId)).thenReturn(Optional.of(existingBoardModel));

        // when
        assertDoesNotThrow(() -> boardService.deleteBoardById(boardId),
                "deleteBoardById should not throw an exception for an existing board");

        // then
        verify(boardRepository, times(1)).findBoardById(boardId);
        verify(boardRepository, times(1)).deleteBoard(boardId);
    }

    @Test
    @DisplayName("given non-existent board when deleteBoardById is called then throws ResourceNotFoundException")
    void givenNonExistentBoard_whenDeleteBoardById_thenThrowsResourceNotFoundException() {
        // given
        Long nonExistentBoardId = 99L;

        // mock
        when(boardRepository.findBoardById(nonExistentBoardId)).thenReturn(Optional.empty());

        // when && then
        assertThrows(ResourceNotFoundException.class,
                () -> boardService.deleteBoardById(nonExistentBoardId),
                "deleteBoardById should throw a ResourceNotFoundException for a non-existent board");
        verify(boardRepository, times(1)).findBoardById(nonExistentBoardId);
    }


}
