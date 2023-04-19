package com.elysium.reddot.ms.board.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.board.application.exception.handler.core.CamelGlobalExceptionHandler;
import com.elysium.reddot.ms.board.application.exception.handler.core.IExceptionHandler;
import com.elysium.reddot.ms.board.application.exception.handler.exceptionhandler.ResourceAlreadyExistIExceptionHandler;
import com.elysium.reddot.ms.board.application.exception.handler.exceptionhandler.ResourceBadValueIExceptionHandler;
import com.elysium.reddot.ms.board.application.exception.handler.exceptionhandler.ResourceNotFoundIExceptionHandler;
import com.elysium.reddot.ms.board.application.service.BoardApplicationServiceImpl;
import com.elysium.reddot.ms.board.infrastructure.constant.BoardRouteConstants;
import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.*;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardRouteBuilderTest extends CamelTestSupport {

    @Mock
    private BoardApplicationServiceImpl boardService;

    @Override
    protected CamelContext createCamelContext() {
        return new DefaultCamelContext();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        List<IExceptionHandler> listIExceptionHandlers = Arrays.asList(
                new ResourceAlreadyExistIExceptionHandler(),
                new ResourceBadValueIExceptionHandler(),
                new ResourceNotFoundIExceptionHandler());
        CamelGlobalExceptionHandler camelGlobalExceptionHandler = new CamelGlobalExceptionHandler(listIExceptionHandlers);

        BoardProcessorHolder boardProcessorHolder = new BoardProcessorHolder(
                new GetAllBoardsProcessor(boardService),
                new GetBoardByIdProcessor(boardService),
                new CreateBoardProcessor(boardService),
                new UpdateBoardProcessor(boardService),
                new DeleteBoardProcessor(boardService)
        );

        return new BoardRouteBuilder(camelGlobalExceptionHandler, boardProcessorHolder);
    }

    @Test
    @DisplayName("given boards exist when route getAllBoards is called then all boards retrieved")
    void givenBoardsExist_whenRouteGetAllBoards_thenAllBoardsRetrieved() {
        // given
        BoardDTO board1 = new BoardDTO(1L, "name 1", "Name 1", "Board 1");
        BoardDTO board2 = new BoardDTO(2L, "name 2", "Name 2", "Board 2");
        List<BoardDTO> boardList = Arrays.asList(board1, board2);

        Exchange exchange = new DefaultExchange(context);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All boards retrieved successfully", boardList);

        // mock
        when(boardService.getAllBoards()).thenReturn(boardList);

        // when
        Exchange responseExchange = template.send("direct:getAllBoards", exchange);
        ApiResponseDTO actualResponse = responseExchange.getIn().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }


    @Test
    @DisplayName("given existing board when route getBoardById is called with valid id then board returned")
    void givenExistingBoard_whenRouteGetBoardByIdWithValidId_thenBoardReturned() {
        // given
        Long boardId = 1L;
        BoardDTO board = new BoardDTO(boardId, "name 1", "Name 1", "Board 1");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", boardId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with id 1 retrieved successfully", board);

        // mock
        when(boardService.getBoardById(boardId)).thenReturn(board);

        // when
        Exchange result = template.send(BoardRouteConstants.GET_BOARD_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given non-existing board id when route getBoardById is called then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingBoardId_whenRouteGetBoardById_thenThrowResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.NOT_FOUND.value(),
                "The board with ID 99 does not exist.", null);

        // mock
        when(boardService.getBoardById(nonExistingId)).thenThrow(new ResourceNotFoundException("board", String.valueOf(nonExistingId)));

        // when
        Exchange result = template.send(BoardRouteConstants.GET_BOARD_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given valid board when route createBoard is called then board created")
    void givenValidBoard_whenRouteCreateBoard_thenBoardCreated() {
        // given
        BoardDTO inputBoard = new BoardDTO(null, "name", "Name", "Description");
        BoardDTO createdBoard = new BoardDTO(1L, inputBoard.getName(), inputBoard.getLabel(), inputBoard.getDescription());

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputBoard);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Board with name " + createdBoard.getName() + " created successfully", createdBoard);

        // mock
        when(boardService.createBoard(inputBoard)).thenReturn(createdBoard);

        // when
        Exchange responseExchange = template.send(BoardRouteConstants.CREATE_BOARD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given board exists when route createBoard is called with creating same board then throws ResourceAlreadyExistExceptionHandler")
    void givenBoardExists_whenRouteCreateBoardWithCreatingSameBoard_thenThrowsResourceAlreadyExistExceptionHandler() {
        // given
        BoardDTO existingBoard = new BoardDTO(1L, "name", "Name", "Board description");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody(existingBoard);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CONFLICT.value(),
                "The board with name 'name' already exists.", null);

        // mock
        when(boardService.createBoard(existingBoard)).thenThrow(new ResourceAlreadyExistException("board", "name", "name"));

        // when
        Exchange responseExchange = template.send(BoardRouteConstants.CREATE_BOARD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given valid request when route updateBoard is called then board is updated")
    void givenValidRequest_whenRouteUpdateBoardIsCalled_thenBoardIsUpdated() {
        // given
        Long boardId = 1L;
        BoardDTO request = new BoardDTO(boardId, "newName", "newDescription", "newIcon");
        BoardDTO updatedBoard = new BoardDTO(boardId, "newName", "newDescription", "newIcon");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", boardId);
        exchange.getIn().setBody(request);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with name " + updatedBoard.getName() + " updated successfully", updatedBoard);

        // mock
        when(boardService.updateBoard(boardId, request)).thenReturn(updatedBoard);

        // when
        Exchange responseExchange = template.send(BoardRouteConstants.UPDATE_BOARD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route updateBoard is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateBoard_thenThrowsResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        BoardDTO request = new BoardDTO(nonExistingId, "newName", "newDescription", "newIcon");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.NOT_FOUND.value(),
                "The board with ID 99 does not exist.", null);

        // mock
        when(boardService.updateBoard(nonExistingId, request)).thenThrow(new ResourceNotFoundException("board", String.valueOf(nonExistingId)));

        // when
        Exchange result = template.send(BoardRouteConstants.UPDATE_BOARD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given board exists when route deleteBoard is called then board deleted")
    void givenBoardExists_whenRouteDeleteBoard_thenBoardDeleted() {
        // given
        Long boardId = 1L;
        BoardDTO boardDTO = new BoardDTO(boardId, "test", "Test", "Test board");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", boardId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with id " + boardId + " deleted successfully", boardDTO);

        // mock
        when(boardService.getBoardById(1L)).thenReturn(boardDTO);

        // when
        Exchange result = template.send(BoardRouteConstants.DELETE_BOARD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // assert
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route deleteBoard is called then throws resourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteDeleteBoard_thenThrowsResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.NOT_FOUND.value(),
                "The board with ID 99 does not exist.", null);

        // mock
        doThrow(new ResourceNotFoundException("board", String.valueOf(nonExistingId)))
                .when(boardService).deleteBoardById(nonExistingId);

        // when
        Exchange result = template.send(BoardRouteConstants.DELETE_BOARD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }


}
