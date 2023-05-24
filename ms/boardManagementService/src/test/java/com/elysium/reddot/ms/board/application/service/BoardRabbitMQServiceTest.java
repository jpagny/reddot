package com.elysium.reddot.ms.board.application.service;

import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.outbound.persistence.BoardRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BoardRabbitMQServiceTest {

    @Mock
    private BoardRepositoryAdapter boardRepository;

    @InjectMocks
    private BoardRabbitMQService boardRabbitMQService;


    @Test
    @DisplayName("given board with specific id exists, when checkBoardIdExists is called, then return true")
    public void givenBoardExists_whenCheckBoardIdExists_thenTrue() {
        // given
        Long boardId = 1L;

        // mock
        when(boardRepository.findBoardById(boardId)).thenReturn(Optional.of(new BoardModel(boardId,"name","label","description",1L)));

        // when
        boolean exists = boardRabbitMQService.checkBoardIdExists(boardId);

        // then
        assertTrue(exists);
    }

    @Test
    @DisplayName("given board with specific id does not exist, when checkBoardIdExists is called, then return false")
    public void givenBoardDoesNotExist_whenCheckBoardIdExists_thenFalse() {
        // given
        Long boardId = 1L;

        // mock
        when(boardRepository.findBoardById(boardId)).thenReturn(Optional.empty());

        // When
        boolean exists = boardRabbitMQService.checkBoardIdExists(boardId);

        // Then
        assertFalse(exists);
    }

}
