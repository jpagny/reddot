package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllBoardsProcessorTest {

    private GetAllBoardsProcessor getAllBoardsProcessor;

    @Mock
    private BoardApplicationServiceImpl boardService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        getAllBoardsProcessor = new GetAllBoardsProcessor(boardService);
    }

    @Test
    @DisplayName("given boards exist when get allBoards is called then all boards retrieved")
    void givenBoardsExist_whenGetAllBoards_thenAllBoardsAreRetrieved() {
        // given
        BoardModel board1Model = new BoardModel(1L, "name 1", "Name 1", "Board 1");
        BoardModel board2Model = new BoardModel(2L, "name 2", "Name 2", "Board 2");
        List<BoardModel> boardListModel = Arrays.asList(board1Model, board2Model);
        List<BoardDTO> expectedListBoards = BoardProcessorMapper.toDTOList(boardListModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All boards retrieved successfully", expectedListBoards);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/boards");

        // mock
        when(boardService.getAllBoards()).thenReturn(boardListModel);

        // when
        getAllBoardsProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
