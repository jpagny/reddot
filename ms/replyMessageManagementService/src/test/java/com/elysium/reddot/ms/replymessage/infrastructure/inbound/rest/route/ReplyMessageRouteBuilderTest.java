package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.data.exception.GlobalExceptionDTO;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage.*;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageProcessorMapper;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitMQ.requester.MessageExistRequester;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReplyMessageRouteBuilderTest extends CamelTestSupport {

    @Mock
    private ReplyMessageApplicationServiceImpl replyMessageService;

    @Mock
    private MessageExistRequester messageExistRequester;

    private ObjectMapper objectMapper;

    @Override
    protected CamelContext createCamelContext() {
        return new DefaultCamelContext();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        ReplyMessageProcessorHolder replyMessageProcessorHolder = new ReplyMessageProcessorHolder(
                new GetAllRepliesMessageProcessor(replyMessageService),
                new GetReplyMessageByIdProcessor(replyMessageService),
                new CreateReplyMessageProcessor(replyMessageService, messageExistRequester),
                new UpdateReplyMessageProcessor(replyMessageService)
        );

        return new ReplyMessageRouteBuilder(globalExceptionHandler, replyMessageProcessorHolder, objectMapper);
    }


    @Test
    @DisplayName("given replyMessages exist when route getAllReplyMessages is called then all replyMessages retrieved")
    void givenReplyMessagesExist_whenRouteGetAllReplyMessages_thenAllReplyMessagesRetrieved() throws JsonProcessingException {
        // given
        ReplyMessageModel replyMessage1Model = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageModel replyMessage2Model = new ReplyMessageModel("content2", 1L, "userId");
        List<ReplyMessageModel> replyMessageListModel = Arrays.asList(replyMessage1Model, replyMessage2Model);
        List<ReplyMessageDTO> expectedListReplyMessages = ReplyMessageProcessorMapper.toDTOList(replyMessageListModel);

        Exchange exchange = new DefaultExchange(context);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All replies message retrieved successfully", expectedListReplyMessages);

        // mock
        when(replyMessageService.getAllRepliesMessage()).thenReturn(replyMessageListModel);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteConstants.GET_ALL_REPLIES_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


    @Test
    @DisplayName("given existing replyMessage when route getReplyMessageById is called with valid id then replyMessage returned")
    void givenExistingReplyMessage_whenRouteGetReplyMessageByIdWithValidId_thenReplyMessageReturned() throws JsonProcessingException {
        // given
        Long replyMessageId = 1L;
        ReplyMessageModel replyMessage = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageDTO expectedReplyMessage = new ReplyMessageDTO("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", replyMessageId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message with id 1 retrieved successfully", expectedReplyMessage);

        // mock
        when(replyMessageService.getReplyMessageById(replyMessageId)).thenReturn(replyMessage);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteConstants.GET_REPLY_MESSAGE_BY_ID.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given non-existing replyMessage id when route getReplyMessageById is called then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingReplyMessageId_whenRouteGetReplyMessageById_thenThrowResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The reply message with ID 99 does not exist.");

        // mock
        when(replyMessageService.getReplyMessageById(nonExistingId)).thenThrow(new ResourceNotFoundException("reply message", String.valueOf(nonExistingId)));

        // when
        Exchange result = template.send(ReplyMessageRouteConstants.GET_REPLY_MESSAGE_BY_ID.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = result.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid replyMessage when route createReplyMessage is called then replyMessage created")
    void givenValidReplyMessage_whenRouteCreateReplyMessage_thenReplyMessageCreated() throws JsonProcessingException {
        // given
        ReplyMessageDTO inputReplyMessageDTO = new ReplyMessageDTO("content", 1L, "userId");
        ReplyMessageModel inputReplyMessageModel = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageModel createdReplyMessageModel = new ReplyMessageModel(inputReplyMessageModel.getContent(), inputReplyMessageModel.getParentMessageID(), inputReplyMessageModel.getUserId());
        ReplyMessageDTO expectedReplyMessage = ReplyMessageProcessorMapper.toDTO(createdReplyMessageModel);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputReplyMessageDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Reply message with content " + expectedReplyMessage.getContent() + " created successfully", expectedReplyMessage);

        // mock
        doNothing().when(messageExistRequester).verifyMessageIdExistsOrThrow(1L);
        when(replyMessageService.createReplyMessage(inputReplyMessageModel)).thenReturn(createdReplyMessageModel);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteConstants.CREATE_REPLY_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given replyMessage exists when route createReplyMessage is called with creating same replyMessage then throws ResourceAlreadyExistExceptionHandler")
    void givenReplyMessageExists_whenRouteCreateReplyMessageWithCreatingSameReplyMessage_thenThrowsResourceAlreadyExistExceptionHandler() {
        // given
        ReplyMessageDTO existingReplyMessageDTO = new ReplyMessageDTO("content", 1L, "userId");
        ReplyMessageModel existingReplyMessageModel = new ReplyMessageModel("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody(existingReplyMessageDTO);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The reply message with name 'name' already exists.");

        // mock
        when(replyMessageService.createReplyMessage(existingReplyMessageModel)).thenThrow(new ResourceAlreadyExistException("reply message", "name", "name"));

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteConstants.CREATE_REPLY_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid request when route updateReplyMessage is called then replyMessage is updated")
    void givenValidRequest_whenRouteUpdateReplyMessageIsCalled_thenReplyMessageIsUpdated() throws JsonProcessingException {
        // given
        Long replyMessageId = 1L;
        ReplyMessageDTO inputReplyMessageDTO = new ReplyMessageDTO("content", 1L, "userId");
        ReplyMessageModel requestModel = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageModel updatedReplyMessage = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageDTO expectedReplyMessage = ReplyMessageProcessorMapper.toDTO(updatedReplyMessage);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", replyMessageId);
        exchange.getIn().setBody(inputReplyMessageDTO);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message with content " + updatedReplyMessage.getContent() + " updated successfully", expectedReplyMessage);

        // mock
        when(replyMessageService.updateReplyMessage(replyMessageId, requestModel)).thenReturn(updatedReplyMessage);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteConstants.UPDATE_REPLY_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid request when route updateReplyMessage is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateReplyMessage_thenThrowsResourceNotFoundExceptionHandler() {
        // given
        Long nonExistingId = 99L;
        ReplyMessageDTO inputRequestDTO = new ReplyMessageDTO("content", 1L, "userId");
        ReplyMessageModel request = new ReplyMessageModel("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(inputRequestDTO);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The reply message with ID 99 does not exist.");

        // mock
        when(replyMessageService.updateReplyMessage(nonExistingId, request)).thenThrow(new ResourceNotFoundException("reply message", String.valueOf(nonExistingId)));

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteConstants.UPDATE_REPLY_MESSAGE.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getMessage().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

}
