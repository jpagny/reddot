package com.elysium.reddot.ms.board.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.board.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.board.application.data.dto.BoardDTO;
import com.elysium.reddot.ms.board.application.exception.ResourceNotFoundException;
import com.elysium.reddot.ms.board.container.TestContainerSetup;
import com.elysium.reddot.ms.board.infrastructure.constant.BoardRouteEnum;
import com.elysium.reddot.ms.board.infrastructure.data.exception.GlobalExceptionDTO;
import com.elysium.reddot.ms.board.infrastructure.outbound.rabbitMQ.requester.TopicExistRequester;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BoardRouteBuilderIT extends TestContainerSetup {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;

    @MockBean
    private TopicExistRequester topicExistRequester;


    @Test
    @DisplayName("given no auth when get boards then unauthorized")
    void givenNoAuth_whenGetBoards_thenUnauthorized() {
        // given
        Exchange exchange = new DefaultExchange(camelContext);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("TokenNotValidException",
                "Token is not validate.");

        // when
        Exchange result = template.send(BoardRouteEnum.GET_ALL_BOARDS.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given authenticated user with token when get boards then success")
    void givenAuthenticatedUserWithToken_whenGetBoards_thenSuccess() throws Exception {
        // given
        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);

        // when
        Exchange result = template.send(BoardRouteEnum.GET_ALL_BOARDS.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(HttpStatus.OK.value(), actualResponse.getStatus());
    }

    @Test
    @DisplayName("given boards exist when route getAllBoards is called then all boards retrieved")
    void givenBoardsExist_whenRouteGetAllBoards_thenAllBoardsAreRetrieved() throws Exception {
        // arrange
        BoardDTO board1 = new BoardDTO(1L, "name_1", "Label 1", "Description 1", 1L);
        BoardDTO board2 = new BoardDTO(2L, "name_2", "Label 2", "Description 2", 1L);
        List<BoardDTO> boardList = Arrays.asList(board1, board2);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);

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
    void givenExistingBoard_whenRouteGetBoardByIdWithValidId_thenBoardReturned() throws Exception {
        // given
        Long boardId = 1L;
        BoardDTO board = new BoardDTO(boardId, "name_1", "Label 1", "Description 1", 1L);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
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
    void givenNonExistingBoardId_whenRouteGetBoardById_thenThrowResourceNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;
        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
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
    void givenValidBoard_whenRouteCreateBoard_thenBoardCreated() throws Exception {
        // given
        BoardDTO inputBoard = new BoardDTO(null, "name_3", "Label 3", "Description 3", 1L);
        BoardDTO createdBoard = new BoardDTO(3L, inputBoard.getName(), inputBoard.getLabel(), inputBoard.getDescription(), 1L);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
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
    void givenInvalidTopic_whenRouteCreateBoard_thenThrowsResourceNotFoundException() throws Exception {
        // given
        BoardDTO inputBoard = new BoardDTO(null, "name_3", "Label 3", "Description 3", 1L);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
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
    void givenBoardExists_whenRouteCreateBoardWithCreatingSameBoard_thenThrowsResourceAlreadyExistExceptionHandler() throws Exception {
        // given
        BoardDTO existingBoard = new BoardDTO(1L, "name_1", "Label 1", "Description 1", 1L);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
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
    void givenValidRequest_whenRouteUpdateBoard_thenBoardUpdated() throws Exception {
        // given
        Long boardId = 1L;
        BoardDTO request = new BoardDTO(boardId, "name_1", "New label", "New description", 1L);
        BoardDTO updatedBoard = new BoardDTO(boardId, "name_1", "New label", "New description", 1L);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
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
    void givenInvalidRequest_whenRouteUpdateBoard_thenResourceThrowsNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;
        BoardDTO request = new BoardDTO(nonExistingId, "newName", "New label", "New Description", 1L);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
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
    void givenInvalidRequestWithBadValue_whenRouteUpdateBoard_thenThrowsResourceBadValueHandler() throws Exception {
        // given
        Long nonExistingId = 1L;
        BoardDTO request = new BoardDTO(nonExistingId, "name_1", null, "New description", 1L);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
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
    void givenBoardExists_whenRouteDeleteBoard_thenBoardDeleted() throws Exception {
        // given
        Long boardId = 1L;
        BoardDTO boardDTO = new BoardDTO(boardId, "name_1", "Label 1", "Description 1", 1L);

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
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
    void givenInvalidRequest_whenRouteDeleteBoard_thenResourceNotFoundExceptionHandler() throws Exception {
        // given
        Long nonExistingId = 99L;

        String token = obtainAccessToken("user1", "test");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + token);
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


    private String obtainAccessToken(String username, String password) throws Exception {

        HttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder("http://localhost:11003/realms/reddot/protocol/openid-connect/token");
        String requestBody = "grant_type=password&client_id=reddot-app&client_secret=H80mMKQZYyXf9S7yQ2cEAxRmXud0uCmU"
                + "&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);
        StringEntity stringEntity = new StringEntity(requestBody, ContentType.APPLICATION_FORM_URLENCODED);

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setEntity(stringEntity);

        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String responseBody = EntityUtils.toString(entity);
            JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } else {
            throw new RuntimeException("No response body");
        }

    }


}
