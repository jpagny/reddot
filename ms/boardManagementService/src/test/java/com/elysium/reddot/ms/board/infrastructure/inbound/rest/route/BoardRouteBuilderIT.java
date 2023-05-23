package com.elysium.reddot.ms.board.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.exception.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.board.container.TestContainerSetup;
import com.elysium.reddot.ms.board.infrastructure.constant.BoardRouteEnum;
import com.elysium.reddot.ms.board.infrastructure.data.exception.GlobalExceptionDTO;
import com.elysium.reddot.ms.board.infrastructure.outbound.rabbitMQ.requester.TopicExistRequester;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BoardRouteBuilderIT extends TestContainerSetup {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;

    @MockBean
    private TopicExistRequester topicExistRequester;

    @Test
    @DisplayName("given boards exist when route getAllBoards is called then all boards retrieved")
    void givenBoardsExist_whenRouteGetAllBoards_thenAllBoardsAreRetrieved() {
        // arrange
        BoardDTO board1 = new BoardDTO(1L, "name_1", "Label 1", "Description 1", 1L);
        BoardDTO board2 = new BoardDTO(2L, "name_2", "Label 2", "Description 2", 1L);
        List<BoardDTO> boardList = Arrays.asList(board1, board2);

        Exchange exchange = new DefaultExchange(camelContext);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All boards retrieved successfully", boardList);

        // act
        Exchange responseExchange = template.send("direct:getAllBoards", exchange);
        ApiResponseDTO actualResponse = responseExchange.getIn().getBody(ApiResponseDTO.class);

        // assert
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given existing board when route getBoardById is called with valid id then board returned")
    void givenExistingBoard_whenRouteGetBoardByIdWithValidId_thenBoardReturned() {
        // given
        Long boardId = 1L;
        BoardDTO board = new BoardDTO(boardId, "name_1", "Label 1", "Description 1", 1L);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", boardId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with id 1 retrieved successfully", board);

        // when
        Exchange result = template.send(BoardRouteEnum.GET_BOARD_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given non-existing board id when route getBoardById then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingBoardId_whenRouteGetBoardById_thenThrowResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The board with ID 99 does not exist.");

        // when
        Exchange result = template.send(BoardRouteEnum.GET_BOARD_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid board when route createBoard is called then board created")
    void givenValidBoard_whenRouteCreateBoard_thenBoardCreated() throws JsonProcessingException {
        // given
        BoardDTO inputBoard = new BoardDTO(null, "name_3", "Label 3", "Description 3", 1L);
        BoardDTO createdBoard = new BoardDTO(3L, inputBoard.getName(), inputBoard.getLabel(), inputBoard.getDescription(), 1L);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputBoard);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Board with name " + createdBoard.getName() + " created successfully", createdBoard);

        // mock
        doNothing().when(topicExistRequester).verifyTopicIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(BoardRouteEnum.CREATE_BOARD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid topic when route createBoard is called then throws ResourceNotFoundException")
    void givenInvalidTopic_whenRouteCreateBoard_thenThrowsResourceNotFoundException() {
        // given
        BoardDTO inputBoard = new BoardDTO(null, "name_3", "Label 3", "Description 3", 1L);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputBoard);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The Topic id with ID 1 does not exist.");

        // mock
        doThrow(new ResourceNotFoundException("Topic id", "1")).when(topicExistRequester).verifyTopicIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(BoardRouteEnum.CREATE_BOARD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given board exists when route createBoard is called with creating same board then throws resourceAlreadyExistExceptionHandler")
    void givenBoardExists_whenRouteCreateBoardWithCreatingSameBoard_thenThrowsResourceAlreadyExistExceptionHandler() {
        // given
        BoardDTO existingBoard = new BoardDTO(1L, "name_1", "Label 1", "Description 1", 1L);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(existingBoard);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The board with name 'name_1' already exists.");

        // mock
        doNothing().when(topicExistRequester).verifyTopicIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(BoardRouteEnum.CREATE_BOARD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid request when route updateBoard is called then board updated")
    void givenValidRequest_whenRouteUpdateBoard_thenBoardUpdated() {
        // given
        Long boardId = 1L;
        BoardDTO request = new BoardDTO(boardId, "name_1", "New label", "New description", 1L);
        BoardDTO updatedBoard = new BoardDTO(boardId, "name_1", "New label", "New description", 1L);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", boardId);
        exchange.getIn().setBody(request);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with name " + updatedBoard.getName() + " updated successfully", updatedBoard);

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
    void givenInvalidRequest_whenRouteUpdateBoard_thenResourceThrowsNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        BoardDTO request = new BoardDTO(nonExistingId, "newName", "New label", "New Description", 1L);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The board with ID 99 does not exist.");

        // when
        Exchange result = template.send(BoardRouteEnum.UPDATE_BOARD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid request with bad value when route updateBoard is called then throws ResourceBadValueHandler")
    void givenInvalidRequestWithBadValue_whenRouteUpdateBoard_thenThrowsResourceBadValueHandler() {
        // given
        Long nonExistingId = 1L;
        BoardDTO request = new BoardDTO(nonExistingId, "name_1", null, "New description", 1L);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceBadValueException",
                "The board has bad value : label is required and cannot be empty.");

        // when
        Exchange result = template.send(BoardRouteEnum.UPDATE_BOARD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given board exists when route deleteBoard is called then board deleted")
    void givenBoardExists_whenRouteDeleteBoard_thenBoardDeleted() {
        // given
        Long boardId = 1L;
        BoardDTO boardDTO = new BoardDTO(boardId, "name_1", "Label 1", "Description 1", 1L);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", boardId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Board with id " + boardId + " deleted successfully", boardDTO);

        // when
        Exchange result = template.send(BoardRouteEnum.DELETE_BOARD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // assert
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route deleteBoard is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteDeleteBoard_thenResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The board with ID 99 does not exist.");

        // when
        Exchange result = template.send(BoardRouteEnum.DELETE_BOARD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

}
