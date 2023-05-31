package com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.replymessage.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.replymessage.application.service.KeycloakService;
import com.elysium.reddot.ms.replymessage.application.service.ReplyMessageApplicationServiceImpl;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.infrastructure.constant.ReplyMessageRouteEnum;
import com.elysium.reddot.ms.replymessage.infrastructure.data.exception.GlobalExceptionDTO;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.exception.GlobalExceptionHandler;
import com.elysium.reddot.ms.replymessage.infrastructure.inbound.rest.processor.replymessage.*;
import com.elysium.reddot.ms.replymessage.infrastructure.mapper.ReplyMessageModelReplyMessageDTOMapper;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.rabbitmq.requester.MessageExistRequester;
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

import java.io.IOException;
import java.net.URISyntaxException;
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
    private KeycloakService keycloakService;

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
                new CreateReplyMessageProcessor(replyMessageService, keycloakService, messageExistRequester, objectMapper),
                new UpdateReplyMessageProcessor(replyMessageService, keycloakService, objectMapper)
        );


        return new ReplyMessageRouteBuilder(globalExceptionHandler, replyMessageProcessorHolder, objectMapper);
    }

    @Test
    @DisplayName("given replyMessages exist when route getAllReplyMessages is called then all replyMessages retrieved")
    void givenReplyMessagesExist_whenRouteGetAllReplyMessages_thenAllReplyMessagesRetrieved() throws IOException, URISyntaxException {
        // given
        ReplyMessageModel replyMessage1Model = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageModel replyMessage2Model = new ReplyMessageModel("content2", 1L, "userId");
        List<ReplyMessageModel> replyMessageListModel = Arrays.asList(replyMessage1Model, replyMessage2Model);
        List<ReplyMessageDTO> expectedListReplyMessages = ReplyMessageModelReplyMessageDTOMapper.toDTOList(replyMessageListModel);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All replies message retrieved successfully", expectedListReplyMessages);

        // mock
        when(replyMessageService.getAllRepliesMessage()).thenReturn(replyMessageListModel);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.GET_ALL_REPLIES_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


    @Test
    @DisplayName("given existing replyMessage when route getReplyMessageById is called with valid id then replyMessage returned")
    void givenExistingReplyMessage_whenRouteGetReplyMessageByIdWithValidId_thenReplyMessageReturned() throws IOException, URISyntaxException {
        // given
        Long replyMessageId = 1L;
        ReplyMessageModel replyMessage = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageDTO expectedReplyMessage = new ReplyMessageDTO("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", replyMessageId);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message with id 1 retrieved successfully", expectedReplyMessage);

        // mock
        when(replyMessageService.getReplyMessageById(replyMessageId)).thenReturn(replyMessage);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.GET_REPLY_MESSAGE_BY_ID.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given non-existing replyMessage id when route getReplyMessageById is called then throw ResourceNotFoundExceptionHandler")
    void givenNonExistingReplyMessageId_whenRouteGetReplyMessageById_thenThrowResourceNotFoundExceptionHandler() throws URISyntaxException, IOException {
        // given
        Long nonExistingId = 99L;

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The reply message with ID 99 does not exist.");

        // mock
        when(replyMessageService.getReplyMessageById(nonExistingId)).thenThrow(new ResourceNotFoundException("reply message", String.valueOf(nonExistingId)));

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.GET_REPLY_MESSAGE_BY_ID.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid replyMessage when route createReplyMessage is called then replyMessage created")
    void givenValidReplyMessage_whenRouteCreateReplyMessage_thenReplyMessageCreated() throws IOException, URISyntaxException {
        // given
        ReplyMessageDTO inputReplyMessageDTO = new ReplyMessageDTO("content", 1L, "userId");
        String inputMessageDTOJson = objectMapper.writeValueAsString(inputReplyMessageDTO);

        ReplyMessageModel inputReplyMessageModel = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageModel createdReplyMessageModel = new ReplyMessageModel(inputReplyMessageModel.getContent(), inputReplyMessageModel.getParentMessageID(), inputReplyMessageModel.getUserId());
        ReplyMessageDTO expectedReplyMessage = ReplyMessageModelReplyMessageDTOMapper.toDTO(createdReplyMessageModel);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(inputMessageDTOJson);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Reply message with content " + expectedReplyMessage.getContent() + " created successfully", expectedReplyMessage);

        // mock
        doNothing().when(messageExistRequester).verifyMessageIdExistsOrThrow(1L);
        when(replyMessageService.createReplyMessage(inputReplyMessageModel)).thenReturn(createdReplyMessageModel);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.CREATE_REPLY_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given replyMessage exists when route createReplyMessage is called with creating same replyMessage then throws ResourceAlreadyExistExceptionHandler")
    void givenReplyMessageExists_whenRouteCreateReplyMessageWithCreatingSameReplyMessage_thenThrowsResourceAlreadyExistExceptionHandler() throws URISyntaxException, IOException {
        // given
        ReplyMessageDTO existingReplyMessageDTO = new ReplyMessageDTO("content", 1L, "userId");
        String inputMessageDTOJson = objectMapper.writeValueAsString(existingReplyMessageDTO);

        ReplyMessageModel existingReplyMessageModel = new ReplyMessageModel("content", 1L, "userId");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setBody(inputMessageDTOJson);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceAlreadyExistException",
                "The reply message with name 'name' already exists with reply message id 1.");

        // mock
        when(replyMessageService.createReplyMessage(existingReplyMessageModel)).thenThrow(new ResourceAlreadyExistException("reply message", "name", "name",1L));

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.CREATE_REPLY_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given valid request when route updateReplyMessage is called then replyMessage is updated")
    void givenValidRequest_whenRouteUpdateReplyMessageIsCalled_thenReplyMessageIsUpdated() throws IOException, URISyntaxException {
        // given
        Long replyMessageId = 1L;
        ReplyMessageDTO inputReplyMessageDTO = new ReplyMessageDTO("content", 1L, "userId");
        String inputMessageDTOJson = objectMapper.writeValueAsString(inputReplyMessageDTO);

        ReplyMessageModel requestModel = new ReplyMessageModel("content", 1L, "userId");
        requestModel.setId(replyMessageId);
        ReplyMessageModel updatedReplyMessage = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageDTO expectedReplyMessage = ReplyMessageModelReplyMessageDTOMapper.toDTO(updatedReplyMessage);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", replyMessageId);
        exchange.getIn().setBody(inputMessageDTOJson);

        // expected
        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Reply message updated successfully", expectedReplyMessage);

        // mock
        when(replyMessageService.updateReplyMessage(replyMessageId, requestModel)).thenReturn(updatedReplyMessage);

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.UPDATE_REPLY_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        ApiResponseDTO actualResponse = objectMapper.readValue(responseJson, ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

    @Test
    @DisplayName("given invalid request when route updateReplyMessage is called then throws ResourceNotFoundExceptionHandler")
    void givenInvalidRequest_whenRouteUpdateReplyMessage_thenThrowsResourceNotFoundExceptionHandler() throws URISyntaxException, IOException {
        // given
        Long nonExistingId = 99L;
        ReplyMessageDTO inputRequestDTO = new ReplyMessageDTO("content", 1L, "userId");
        String inputMessageDTOJson = objectMapper.writeValueAsString(inputRequestDTO);

        ReplyMessageModel request = new ReplyMessageModel("content", 1L, "userId");
        request.setId(nonExistingId);

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        exchange.getIn().setHeader("id", nonExistingId);
        exchange.getIn().setBody(inputMessageDTOJson);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("ResourceNotFoundException",
                "The reply message with ID 99 does not exist.");

        // mock
        when(replyMessageService.updateReplyMessage(nonExistingId, request)).thenThrow(new ResourceNotFoundException("reply message", String.valueOf(nonExistingId)));

        // when
        Exchange responseExchange = template.send(ReplyMessageRouteEnum.UPDATE_REPLY_MESSAGE.getRouteName(), exchange);
        String responseJson = responseExchange.getMessage().getBody(String.class);
        GlobalExceptionDTO actualResponse = objectMapper.readValue(responseJson, GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }

}
