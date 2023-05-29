package com.elysium.reddot.ms.board.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.data.mapper.BoardDTOBoardModelMapper;
import com.elysium.reddot.ms.board.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.board.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.domain.model.BoardModel;
import com.elysium.reddot.ms.board.infrastructure.constant.BoardRouteEnum;
import com.elysium.reddot.ms.board.infrastructure.data.dto.GlobalExceptionDTO;
import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.board.*;
import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.board.infrastructure.outbound.rabbitmq.requester.TopicExistRequester;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Policy;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.SimpleRegistry;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardRouteBuilderTest extends CamelTestSupport {

    @Mock
    private BoardApplicationServiceImpl boardService;

    @Mock
    private TopicExistRequester topicExistRequester;

    @Override
    protected CamelContext createCamelContext() {
        SimpleRegistry registry = new SimpleRegistry();
        registry.bind("adminPolicy", new MockPolicy());
        return new DefaultCamelContext(registry);
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        BoardProcessorHolder boardProcessorHolder = new BoardProcessorHolder(
                new GetAllBoardsProcessor(boardService),
                new GetBoardByIdProcessor(boardService),
                new CreateBoardProcessor(boardService, topicExistRequester),
                new UpdateBoardProcessor(boardService)
        );

        return new BoardRouteBuilder(globalExceptionHandler, boardProcessorHolder);
    }

    @Test
    @DisplayName("given existing board when route getBoardById is called with valid id then board returned")
    void givenExistingBoard_whenRouteGetBoardByIdWithValidId_thenBoardReturned() throws Exception {
        // given
        Long boardId = 1L;
        BoardModel board = new BoardModel(boardId, "name 1", "Name 1", "Board 1", 1L);
        BoardDTO expectedBoard = new BoardDTO(boardId, "name 1", "Name 1", "Board 1", 1L);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", boardId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with id 1 retrieved successfully", expectedBoard);

        // mock
        when(boardService.getBoardById(boardId)).thenReturn(board);

        // when
        Exchange result = template.send(BoardRouteEnum.GET_BOARD_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given non-existing board id when route getBoardById is called then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingBoardId_whenRouteGetBoardById_thenThrowResourceNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The board with ID 99 does not exist.");

        // mock
        when(boardService.getBoardById(nonExistingId)).thenThrow(new ResourceNotFoundException("board", String.valueOf(nonExistingId)));

        // when
        Exchange result = template.send(BoardRouteEnum.GET_BOARD_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid board when route createBoard is called then board created")
    void givenValidBoard_whenRouteCreateBoard_thenBoardCreated() throws Exception {
        // given
        BoardDTO inputBoardDTO = new BoardDTO(null, "name", "Name", "Description", 1L);
        BoardModel inputBoardModel = new BoardModel(null, "name", "Name", "Description", 1L);
        BoardModel createdBoardModel = new BoardModel(1L, inputBoardModel.getName(), inputBoardModel.getLabel(), inputBoardModel.getDescription(), 1L);
        BoardDTO expectedBoard = BoardDTOBoardModelMapper.toDTO(createdBoardModel);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputBoardDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Board with name " + expectedBoard.getName() + " created successfully", expectedBoard);

        // mock
        when(boardService.createBoard(inputBoardModel)).thenReturn(createdBoardModel);

        // when
        Exchange responseExchange = template.send(BoardRouteEnum.CREATE_BOARD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given board exists when route createBoard is called with creating same board then throws ResourceAlreadyExistExceptionHandler")
    void givenBoardExists_whenRouteCreateBoardWithCreatingSameBoard_thenThrowsResourceAlreadyExistExceptionHandler() throws Exception {
        // given
        BoardDTO existingBoardDTO = new BoardDTO(1L, "name", "Name", "Board description", 1L);
        BoardModel existingBoardModel = new BoardModel(1L, "name", "Name", "Board description", 1L);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setBody(existingBoardDTO);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The board with name 'name' already exists.");

        // mock
        when(boardService.createBoard(existingBoardModel)).thenThrow(new ResourceAlreadyExistException("board", "name", "name"));

        // when
        Exchange responseExchange = template.send(BoardRouteEnum.CREATE_BOARD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid request when route updateBoard is called then board is updated")
    void givenValidRequest_whenRouteUpdateBoardIsCalled_thenBoardIsUpdated() throws Exception {
        // given
        Long boardId = 1L;
        BoardDTO inputBoardDTO = new BoardDTO(boardId, "newName", "newDescription", "newIcon", 1L);
        BoardModel requestModel = new BoardModel(boardId, "newName", "newDescription", "newIcon", 1L);
        BoardModel updatedBoard = new BoardModel(boardId, "newName", "newDescription", "newIcon", 1L);
        BoardDTO expectedBoard = BoardDTOBoardModelMapper.toDTO(updatedBoard);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", boardId);
        exchange.getIn().setBody(inputBoardDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with name " + updatedBoard.getName() + " updated successfully", expectedBoard);

        // mock
        when(boardService.updateBoard(boardId, requestModel)).thenReturn(updatedBoard);

        // when
        Exchange responseExchange = template.send(BoardRouteEnum.UPDATE_BOARD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route updateBoard is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateBoard_thenThrowsResourceNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;
        BoardDTO inputRequestDTO = new BoardDTO(nonExistingId, "newName", "newDescription", "newIcon", 1L);
        BoardModel request = new BoardModel(nonExistingId, "newName", "newDescription", "newIcon", 1L);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(inputRequestDTO);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The board with ID 99 does not exist.");

        // mock
        when(boardService.updateBoard(nonExistingId, request)).thenThrow(new ResourceNotFoundException("board", String.valueOf(nonExistingId)));

        // when
        Exchange result = template.send(BoardRouteEnum.UPDATE_BOARD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

}

class MockPolicy implements Policy {

    @Override
    public void beforeWrap(Route route, NamedNode definition) {

    }

    @Override
    public Processor wrap(Route route, Processor processor) {
        return processor;
    }
}
