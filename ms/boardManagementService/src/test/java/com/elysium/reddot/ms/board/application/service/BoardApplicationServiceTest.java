package com.elysium.reddot.ms.board.application.service;

import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceBadValueException;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.board.domain.service.BoardDomainService;
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
        BoardDomainService boardDomainService = new BoardDomainService();
        boardService = new BoardApplicationServiceImpl(boardDomainService, boardRepository);
    }

    @Test
    @DisplayName("given valid id when getBoardById is called then returns board")
    void givenValidId_whenGetBoardById_thenReturnsBoard() {
        // given
        Long validId = 1L;
        BoardDTO expectedBoard = new BoardDTO(1L, "test", "Test Name", "Test Description");

        // mock
        when(boardRepository.findBoardById(validId)).thenReturn(Optional.of(expectedBoard));

        // when
        BoardDTO actualBoard = boardService.getBoardById(validId);

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
    @DisplayName("given existing boards, when getAll, then returns List of boardDTO")
    void givenExistingBoards_whenGetAll_thenReturnsListOfBoardDTOs() {
        // given
        BoardDTO board1 = new BoardDTO(1L, "test1", "Test Name 1", "Test Description 1");
        BoardDTO board2 = new BoardDTO(2L, "test2", "Test Name 2", "Test Description 2");

        // mock
        List<BoardDTO> expectedBoards = Arrays.asList(board1, board2);
        when(boardService.getAllBoards()).thenReturn(expectedBoards);

        // when
        List<BoardDTO> actualBoards = boardService.getAllBoards();

        // then
        assertEquals(expectedBoards, actualBoards, "The returned board list should match the expected board list");
        verify(boardRepository, times(1)).findAllBoards();
    }

    @Test
    @DisplayName("given valid boardDTO when createBoard is called then board created")
    void givenValidBoardDTO_whenCreateBoard_thenBoardCreated() {
        // given
        BoardDTO boardToCreateDTO = new BoardDTO(null, "test", "Test Label", "Test Description");
        BoardDTO createdBoardDTO = new BoardDTO(1L, "test 2", "Test Label", "Test Description");

        // mock
        when(boardRepository.findBoardByName(boardToCreateDTO.getName())).thenReturn(Optional.empty());
        when(boardRepository.createBoard(boardToCreateDTO)).thenReturn(createdBoardDTO);

        // when
        BoardDTO actualBoardDTO = boardService.createBoard(boardToCreateDTO);

        // then
        assertEquals(createdBoardDTO, actualBoardDTO, "The created board should match the expected board");
        verify(boardRepository, times(1)).findBoardByName(boardToCreateDTO.getName());
        verify(boardRepository, times(1)).createBoard(boardToCreateDTO);
    }

    @Test
    @DisplayName("given existing board when CreateBoard is called then throws ResourceAlreadyExistException")
    void givenExistingBoard_whenCreateBoard_thenThrowsResourceAlreadyExistException() {
        // given
        BoardDTO existingBoardDTO = new BoardDTO(1L, "Test Name", "Test Label", "Test Description");

        // mock
        when(boardRepository.findBoardByName(existingBoardDTO.getName())).thenReturn(Optional.of(existingBoardDTO));

        // when && then
        assertThrows(ResourceAlreadyExistException.class,
                () -> boardService.createBoard(existingBoardDTO),
                "createBoard should throw a ResourceAlreadyExistException for an existing board");
        verify(boardRepository, times(1)).findBoardByName(existingBoardDTO.getName());
    }

    @Test
    @DisplayName("given invalid boardDTO when createBoard is called then throws ResourceBadValueException")
    void givenInvalidBoardDTO_whenCreateBoard_thenThrowsResourceBadValueException() {
        // given
        BoardDTO invalidBoardDTO = new BoardDTO(1L, "", "Invalid Label", "Invalid Description");

        // mock
        when(boardRepository.findBoardByName(invalidBoardDTO.getName())).thenReturn(Optional.empty());

        // when && then
        assertThrows(ResourceBadValueException.class,
                () -> boardService.createBoard(invalidBoardDTO),
                "createBoard should throw a ResourceBadValueException for an invalid board");
        verify(boardRepository, times(1)).findBoardByName(invalidBoardDTO.getName());
    }

    @Test
    @DisplayName("given validBoardDTO when updateBoard is called then board updated")
    void givenValidBoardDTO_whenUpdateBoard_thenBoardUpdated() {
        // given
        Long boardId = 1L;
        BoardDTO existingBoardDTO = new BoardDTO(boardId, "Old Name", "OldLabel", "Old Description");
        BoardDTO boardToUpdateDTO = new BoardDTO(boardId, "Old Name", "NewLabel", "NewDescription");
        BoardDTO updatedBoardDTO = new BoardDTO(boardId, "Old Name", "NewLabel", "NewDescription");

        // mock
        when(boardRepository.findBoardById(boardId)).thenReturn(Optional.of(existingBoardDTO));
        when(boardRepository.updateBoard(updatedBoardDTO)).thenReturn(updatedBoardDTO);

        // when
        BoardDTO actualBoardDTO = boardService.updateBoard(1L, boardToUpdateDTO);

        // then
        assertEquals(updatedBoardDTO, actualBoardDTO, "The updated board should match the expected board");
        verify(boardRepository, times(1)).findBoardById(boardId);
        verify(boardRepository, times(1)).updateBoard(updatedBoardDTO);
    }

    @Test
    @DisplayName("given non-existent board when updateBoard is called then throws ResourceNotFoundException")
    void givenNonExistentBoard_whenUpdateBoard_thenThrowsResourceNotFoundException() {
        // given
        Long nonExistentBoardId = 99L;
        BoardDTO boardToUpdateDTO = new BoardDTO(nonExistentBoardId, "New Name", "New Label", "New Description");

        // mock
        when(boardRepository.findBoardById(nonExistentBoardId)).thenReturn(Optional.empty());

        // then && when
        assertThrows(ResourceNotFoundException.class,
                () -> boardService.updateBoard(nonExistentBoardId, boardToUpdateDTO),
                "updateBoard should throw a ResourceNotFoundException for a non-existent board");
        verify(boardRepository, times(1)).findBoardById(nonExistentBoardId);
    }

    @Test
    @DisplayName("given invalid boardDTO when updateBoard is called then throws ResourceBadValueException")
    void givenInvalidBoardDTO_whenUpdateBoard_thenThrowsResourceBadValueException() {
        // given
        Long boardId = 1L;
        BoardDTO existingBoardDTO = new BoardDTO(boardId, "Old Name", "Old Label", "Old Description");
        BoardDTO invalidBoardToUpdateDTO = new BoardDTO(boardId, "Invalid Name", "", "Invalid Description");

        // mock
        when(boardRepository.findBoardById(boardId)).thenReturn(Optional.of(existingBoardDTO));

        // when && then
        assertThrows(ResourceBadValueException.class,
                () -> boardService.updateBoard
                        (boardId, invalidBoardToUpdateDTO),
                "updateBoard should throw a ResourceBadValueException for an invalid board");
        verify(boardRepository, times(1)).findBoardById(boardId);
    }

    @Test
    @DisplayName("given existing board when deleteBoardById is called then board deleted")
    void givenExistingBoard_whenDeleteBoardById_thenBoardDeleted() {
        // given
        Long boardId = 1L;
        BoardDTO existingBoardDTO = new BoardDTO(boardId, "Test Name", "Test Label", "Test Description");

        // mock
        when(boardRepository.findBoardById(boardId)).thenReturn(Optional.of(existingBoardDTO));

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
