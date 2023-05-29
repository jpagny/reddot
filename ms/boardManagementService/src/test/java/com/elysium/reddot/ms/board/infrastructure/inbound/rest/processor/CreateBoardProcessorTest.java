package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board.CreateBoardProcessor;
import com.elysium.reddot.ms.board.infrastructure.outbound.rabbitmq.requester.TopicExistRequester;
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
class CreateBoardProcessorTest {

    private CreateBoardProcessor createBoardProcessor;

    @Mock
    private BoardApplicationServiceImpl boardService;
    @Mock
    private TopicExistRequester topicExistRequester;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        createBoardProcessor = new CreateBoardProcessor(boardService, topicExistRequester);
    }

    @Test
    @DisplayName("given valid board when createBoard is called then board created successfully")
    void givenValidBoard_whenCreateBoard_thenBoardCreatedSuccessfully() throws Exception {
        // given
        BoardDTO boardToCreateDTO = new BoardDTO(null, "name", "Name", "Board description", 1L);
        BoardModel boardToCreateModel = new BoardModel(null, "name", "Name", "Board description", 1L);
        BoardModel createdBoardModel = new BoardModel(1L, "name", "Name", "Board description", 1L);
        BoardDTO expectedBoard = BoardProcessorMapper.toDTO(createdBoardModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Board with name " + createdBoardModel.getName() + " created successfully", expectedBoard);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/boards");
        exchange.getIn().setBody(boardToCreateDTO);

        // mock
        when(boardService.createBoard(boardToCreateModel)).thenReturn(createdBoardModel);

        // when
        createBoardProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
