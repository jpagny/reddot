package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.data.mapper.BoardDTOBoardModelMapper;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board.UpdateBoardProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateBoardProcessorTest {

    private UpdateBoardProcessor updateBoardProcessor;

    @Mock
    private BoardApplicationServiceImpl boardService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        updateBoardProcessor = new UpdateBoardProcessor(boardService);
    }

    @Test
    @DisplayName("given valid board when updateBoard is called then board updated successfully")
    void givenValidBoard_whenUpdateBoard_thenBoardIsUpdatedSuccessfully() {
        // given
        Long boardId = 1L;
        BoardDTO updatedBoardDTO = new BoardDTO(boardId, "new_name", "New Name", "New Board description",1L);
        BoardModel updatedBoardModel = new BoardModel(boardId, "new_name", "New Name", "New Board description",1L);
        BoardDTO expectedBoard = BoardDTOBoardModelMapper.toDTO(updatedBoardModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with name " + updatedBoardModel.getName() + " updated successfully", expectedBoard);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/boards/" + boardId);
        exchange.getIn().setHeader("id", boardId);
        exchange.getIn().setBody(updatedBoardDTO);

        // mock
        when(boardService.updateBoard(boardId, updatedBoardModel)).thenReturn(updatedBoardModel);

        // when
        updateBoardProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }

}
