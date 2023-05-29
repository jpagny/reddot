package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteBoardProcessorTest {

    private DeleteBoardProcessor deleteBoardProcessor;

    @Mock
    private BoardApplicationServiceImpl boardService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        deleteBoardProcessor = new DeleteBoardProcessor(boardService);
    }

    @Test
    @DisplayName("given valid boardId when deleteBoard is called then board deleted Successfully")
    void givenValidBoardId_whenDeleteBoard_thenBoardIsDeletedSuccessfully() {
        // given
        Long boardId = 1L;
        BoardModel boardToDeleteModel = new BoardModel(boardId, "name", "Name", "Board description",1L);
        BoardDTO expectedBoard = BoardProcessorMapper.toDTO(boardToDeleteModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with id " + boardId + " deleted successfully", expectedBoard);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/boards/" + boardId);
        exchange.getIn().setHeader("id", boardId);

        // mock
        when(boardService.deleteBoardById(boardId)).thenReturn(boardToDeleteModel);

        // when
        deleteBoardProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());

        // verify
        verify(boardService).deleteBoardById(boardId);
    }

}
