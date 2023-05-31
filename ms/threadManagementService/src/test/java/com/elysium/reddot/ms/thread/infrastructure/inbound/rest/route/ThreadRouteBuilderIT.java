package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.thread.container.TestContainerSetup;
import com.elysium.reddot.ms.thread.infrastructure.constant.ThreadRouteEnum;
import com.elysium.reddot.ms.thread.infrastructure.data.dto.GlobalExceptionDTO;
import com.elysium.reddot.ms.thread.infrastructure.outbound.rabbitmq.requester.BoardExistRequester;
import com.elysium.reddot.ms.thread.utils.KeycloakTestUtils;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ThreadRouteBuilderIT extends TestContainerSetup {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;

    @MockBean
    private BoardExistRequester boardExistRequester;

    private KeycloakTestUtils.UserToken userToken;

    @BeforeEach
    public void setup() throws Exception {
        userToken = KeycloakTestUtils.obtainAccessToken("user1", "test");
        KeycloakAuthenticationToken auth = KeycloakTestUtils.createAuthenticationToken(userToken, "user");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("given threads exist when route getAllThreads is called then all threads retrieved")
    void givenThreadsExist_whenRouteGetAllThreads_thenAllThreadsAreRetrieved() {
        // arrange
        ThreadDTO thread1 = new ThreadDTO(1L, "name_1", "Label 1", "Description 1", 1L, "bfcff13a-51c9-4e95-b362-639984413fb4");
        ThreadDTO thread2 = new ThreadDTO(2L, "name_2", "Label 2", "Description 2", 1L, "bfcff13a-51c9-4e95-b362-639984413fb4");
        List<ThreadDTO> threadList = Arrays.asList(thread1, thread2);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All threads retrieved successfully", threadList);

        // act
        Exchange responseExchange = template.send("direct:getAllThreads", exchange);
        ApiResponseDTO actualResponse = responseExchange.getIn().getBody(ApiResponseDTO.class);

        // assert
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given existing thread when route getThreadById is called with valid id then thread returned")
    void givenExistingThread_whenRouteGetThreadByIdWithValidId_thenThreadReturned() {
        // given
        Long threadId = 1L;
        ThreadDTO thread = new ThreadDTO(threadId, "name_1", "Label 1", "Description 1", 1L, "bfcff13a-51c9-4e95-b362-639984413fb4");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", threadId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Thread with id 1 retrieved successfully", thread);

        // when
        Exchange result = template.send(ThreadRouteEnum.GET_THREAD_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given non-existing thread id when route getThreadById then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingThreadId_whenRouteGetThreadById_thenThrowResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The thread with ID 99 does not exist.");

        // when
        Exchange result = template.send(ThreadRouteEnum.GET_THREAD_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid thread when route createThread is called then thread created")
    void givenValidThread_whenRouteCreateThread_thenThreadCreated() throws Exception {
        // given
        ThreadDTO inputThread = new ThreadDTO(null, "name_3", "Label 3", "Description 3", 1L, "userId");
        ThreadDTO createdThread = new ThreadDTO(3L, inputThread.getName(), inputThread.getLabel(), inputThread.getDescription(), 1L, "bfcff13a-51c9-4e95-b362-639984413fb4");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputThread);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Thread with name " + createdThread.getName() + " created successfully", createdThread);

        // mock
        doNothing().when(boardExistRequester).verifyBoardIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(ThreadRouteEnum.CREATE_THREAD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid topic when route createThread is called then throws ResourceNotFoundException")
    void givenInvalidTopic_whenRouteCreateThread_thenThrowsResourceNotFoundException() throws Exception {
        // given
        ThreadDTO inputThread = new ThreadDTO(null, "name_3", "Label 3", "Description 3", 1L, "userId");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputThread);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The Topic id with ID 1 does not exist.");

        // mock
        doThrow(new ResourceNotFoundException("Topic id", "1")).when(boardExistRequester).verifyBoardIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(ThreadRouteEnum.CREATE_THREAD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given thread exists when route createThread is called with creating same thread then throws resourceAlreadyExistExceptionHandler")
    void givenThreadExists_whenRouteCreateThreadWithCreatingSameThread_thenThrowsResourceAlreadyExistExceptionHandler() throws Exception {
        // given
        ThreadDTO existingThread = new ThreadDTO(1L, "name_1", "Label 1", "Description 1", 1L, "userId");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setBody(existingThread);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The thread with name 'name_1' already exists with board id 1.");

        // mock
        doNothing().when(boardExistRequester).verifyBoardIdExistsOrThrow(1L);

        // when
        Exchange responseExchange = template.send(ThreadRouteEnum.CREATE_THREAD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid request when route updateThread is called then thread updated")
    void givenValidRequest_whenRouteUpdateThread_thenThreadUpdated() {
        // given
        Long threadId = 1L;
        ThreadDTO request = new ThreadDTO(threadId, "name_1", "New label", "New description", 1L, "");
        ThreadDTO updatedThread = new ThreadDTO(threadId, "name_1", "New label", "New description", 1L, "bfcff13a-51c9-4e95-b362-639984413fb4");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", threadId);
        exchange.getIn().setBody(request);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Thread with name " + updatedThread.getName() + " updated successfully", updatedThread);

        // when
        Exchange responseExchange = template.send(ThreadRouteEnum.UPDATE_THREAD.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route updateThread is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateThread_thenResourceThrowsNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        ThreadDTO request = new ThreadDTO(nonExistingId, "newName", "New label", "New Description", 1L, "userId");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The thread with ID 99 does not exist.");

        // when
        Exchange result = template.send(ThreadRouteEnum.UPDATE_THREAD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid request with bad value when route updateThread is called then throws ResourceBadValueHandler")
    void givenInvalidRequestWithBadValue_whenRouteUpdateThread_thenThrowsResourceBadValueHandler() {
        // given
        Long nonExistingId = 1L;
        ThreadDTO request = new ThreadDTO(nonExistingId, "name_1", null, "New description", 1L, "");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer " + userToken.getAccessToken());
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(request);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceBadValueException",
                "The thread has bad value : label is required and cannot be empty.");

        // when
        Exchange result = template.send(ThreadRouteEnum.UPDATE_THREAD.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


}

