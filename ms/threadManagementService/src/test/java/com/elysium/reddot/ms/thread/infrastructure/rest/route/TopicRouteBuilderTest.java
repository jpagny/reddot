package com.elysium.reddot.ms.thread.infrastructure.rest.route;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.data.dto.GlobalExceptionDTO;
import com.elysium.reddot.ms.thread.infrastructure.exception.processor.GlobalExceptionHandler;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.*;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.route.ThreadRouteBuilder;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.route.ThreadRouteConstants;
import com.elysium.reddot.ms.thread.infrastructure.mapper.ThreadProcessorMapper;
import com.elysium.reddot.ms.thread.infrastructure.outbound.rabbitMQ.requester.BoardExistRequester;
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
class ThreadRouteBuilderTest extends CamelTestSupport {

    @Mock
    private ThreadApplicationServiceImpl threadService;

    @Mock
    private BoardExistRequester boardExistRequester;


    @Override
    protected CamelContext createCamelContext() {
        return new DefaultCamelContext();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        ThreadProcessorHolder threadProcessorHolder = new ThreadProcessorHolder(
                new GetAllThreadsProcessor(threadService),
                new GetThreadByIdProcessor(threadService),
                new CreateThreadProcessor(threadService, boardExistRequester),
                new UpdateThreadProcessor(threadService),
                new DeleteThreadProcessor(threadService)
        );

        return new ThreadRouteBuilder(globalExceptionHandler, threadProcessorHolder);
    }

    @Test
    @DisplayName("given threads exist when route getAllThreads is called then all threads retrieved")
    void givenThreadsExist_whenRouteGetAllThreads_thenAllThreadsRetrieved() {
        // given
        ThreadModel thread1Model = new ThreadModel(1L, "name 1", "Name 1", "Thread 1", 1L, "userId");
        ThreadModel thread2Model = new ThreadModel(2L, "name 2", "Name 2", "Thread 2", 1L, "userId");
        List<ThreadModel> threadListModel = Arrays.asList(thread1Model, thread2Model);
        List<ThreadDTO> expectedListThreads = ThreadProcessorMapper.toDTOList(threadListModel);

        Exchange exchange = new DefaultExchange(context);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All threads retrieved successfully", expectedListThreads);

        // mock
        when(threadService.getAllThreads()).thenReturn(threadListModel);

        // when
        Exchange responseExchange = template.send("direct:getAllThreads", exchange);
        ApiResponseDTO actualResponse = responseExchange.getIn().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }


    @Test
    @DisplayName("given existing thread when route getThreadById is called with valid id then thread returned")
    void givenExistingThread_whenRouteGetThreadByIdWithValidId_thenThreadReturned() {
        // given
        Long threadId = 1L;
        ThreadModel thread = new ThreadModel(threadId, "name 1", "Name 1", "Thread 1", 1L, "userId");
        ThreadDTO expectedThread = new ThreadDTO(threadId, "name 1", "Name 1", "Thread 1", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", threadId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Thread with id 1 retrieved successfully", expectedThread);

        // mock
        when(threadService.getThreadById(threadId)).thenReturn(thread);

        // when
        Exchange result = template.send(ThreadRouteConstants.GET_TOPIC_BY_ID.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given non-existing thread id when route getThreadById is called then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingThreadId_whenRouteGetThreadById_thenThrowResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The thread with ID 99 does not exist.");

        // mock
        when(threadService.getThreadById(nonExistingId)).thenThrow(new ResourceNotFoundException("thread", String.valueOf(nonExistingId)));

        // when
        Exchange result = template.send(ThreadRouteConstants.GET_TOPIC_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid thread when route createThread is called then thread created")
    void givenValidThread_whenRouteCreateThread_thenThreadCreated() {
        // given
        ThreadDTO inputThreadDTO = new ThreadDTO(null, "name", "Name", "Description", 1L, "userId");
        ThreadModel inputThreadModel = new ThreadModel(null, "name", "Name", "Description", 1L, "userId");
        ThreadModel createdThreadModel = new ThreadModel(1L,
                inputThreadModel.getName(),
                inputThreadModel.getLabel(),
                inputThreadModel.getDescription(),
                inputThreadModel.getBoardId(),
                inputThreadModel.getUserId());
        ThreadDTO expectedThread = ThreadProcessorMapper.toDTO(createdThreadModel);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputThreadDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Thread with name " + expectedThread.getName() + " created successfully", expectedThread);

        // mock
        when(threadService.createThread(inputThreadModel)).thenReturn(createdThreadModel);

        // when
        Exchange responseExchange = template.send(ThreadRouteConstants.CREATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given thread exists when route createThread is called with creating same thread then throws ResourceAlreadyExistExceptionHandler")
    void givenThreadExists_whenRouteCreateThreadWithCreatingSameThread_thenThrowsResourceAlreadyExistExceptionHandler() {
        // given
        ThreadDTO existingThreadDTO = new ThreadDTO(1L, "name", "Name", "Thread description", 1L, "userId");
        ThreadModel existingThreadModel = new ThreadModel(1L, "name", "Name", "Thread description", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody(existingThreadDTO);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The thread with name 'name' already exists.");

        // mock
        when(threadService.createThread(existingThreadModel)).thenThrow(new ResourceAlreadyExistException("thread", "name", "name"));

        // when
        Exchange responseExchange = template.send(ThreadRouteConstants.CREATE_TOPIC.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid request when route updateThread is called then thread is updated")
    void givenValidRequest_whenRouteUpdateThreadIsCalled_thenThreadIsUpdated() {
        // given
        Long threadId = 1L;
        ThreadDTO inputThreadDTO = new ThreadDTO(threadId, "newName", "newDescription", "newIcon", 1L, "userId");
        ThreadModel requestModel = new ThreadModel(threadId, "newName", "newDescription", "newIcon", 1L, "userId");
        ThreadModel updatedThread = new ThreadModel(threadId, "newName", "newDescription", "newIcon", 1L, "userId");
        ThreadDTO expectedThread = ThreadProcessorMapper.toDTO(updatedThread);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", threadId);
        exchange.getIn().setBody(inputThreadDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Thread with name " + updatedThread.getName() + " updated successfully", expectedThread);

        // mock
        when(threadService.updateThread(threadId, requestModel)).thenReturn(updatedThread);

        // when
        Exchange responseExchange = template.send(ThreadRouteConstants.UPDATE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = responseExchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route updateThread is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateThread_thenThrowsResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        ThreadDTO inputRequestDTO = new ThreadDTO(nonExistingId, "newName", "newDescription", "newIcon", 1L, "userId");
        ThreadModel request = new ThreadModel(nonExistingId, "newName", "newDescription", "newIcon", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(inputRequestDTO);

        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The thread with ID 99 does not exist.");

        // mock
        when(threadService.updateThread(nonExistingId, request)).thenThrow(new ResourceNotFoundException("thread", String.valueOf(nonExistingId)));

        // when
        Exchange responseExchange = template.send(ThreadRouteConstants.UPDATE_TOPIC.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given thread exists when route deleteThread is called then thread deleted")
    void givenThreadExists_whenRouteDeleteThread_thenThreadDeleted() {
        // given
        Long threadId = 1L;
        ThreadModel threadModel = new ThreadModel(threadId, "test", "Test", "Test thread", 1L, "userId");
        ThreadDTO expectedThread = new ThreadDTO(threadId, "test", "Test", "Test thread", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", threadId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Thread with id " + threadId + " deleted successfully", expectedThread);

        // mock
        when(threadService.deleteThreadById(1L)).thenReturn(threadModel);

        // when
        Exchange result = template.send(ThreadRouteConstants.DELETE_TOPIC.getRouteName(), exchange);
        ApiResponseDTO actualResponse = result.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("given invalid request when route deleteThread is called then throws resourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteDeleteThread_thenThrowsResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The thread with ID 99 does not exist.");

        // mock
        doThrow(new ResourceNotFoundException("thread", String.valueOf(nonExistingId)))
                .when(threadService).deleteThreadById(nonExistingId);

        // when
        Exchange responseExchange = template.send(ThreadRouteConstants.DELETE_TOPIC.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


}
