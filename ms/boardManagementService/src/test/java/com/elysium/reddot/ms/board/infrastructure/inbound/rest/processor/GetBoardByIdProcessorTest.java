package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board.GetBoardByIdProcessor;
import com.elysium.reddot.ms.board.infrastructure.mapper.BoardProcessorMapper;
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
class GetBoardByIdProcessorTest {

    private GetBoardByIdProcessor getBoardByIdProcessor;

    @Mock
    private BoardApplicationServiceImpl boardService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        getBoardByIdProcessor = new GetBoardByIdProcessor(boardService);
    }

    @Test
    @DisplayName("given board exists when getBoardById is called then board retrieved")
    void givenBoardExists_whenGetBoardById_thenBoardIsRetrieved() {
        // given
        Long id = 1L;
        BoardModel boardModel = new BoardModel(id, "name 1", "Name 1", "Board 1",1L);
        BoardDTO expectedBoard = BoardProcessorMapper.toDTO(boardModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with id " + id + " retrieved successfully", expectedBoard);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/boards/" + id);
        exchange.getIn().setHeader("id", id);

        // mock
        when(boardService.getBoardById(id)).thenReturn(boardModel);

        // when
        getBoardByIdProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }

}
