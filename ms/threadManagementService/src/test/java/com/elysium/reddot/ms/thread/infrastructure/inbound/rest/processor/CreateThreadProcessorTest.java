package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.service.KeycloakService;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.thread.CreateThreadProcessor;
import com.elysium.reddot.ms.thread.application.data.mapper.ThreadDTOThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.outbound.rabbitmq.requester.BoardExistRequester;
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

import javax.naming.AuthenticationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateThreadProcessorTest {

    private CreateThreadProcessor createThreadProcessor;

    @Mock
    private ThreadApplicationServiceImpl threadService;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    BoardExistRequester boardExistRequester;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        createThreadProcessor = new CreateThreadProcessor(threadService, keycloakService, boardExistRequester);
    }

    @Test
    @DisplayName("given valid thread when createThread is called then thread created successfully")
    void givenValidThread_whenCreateThread_thenThreadCreatedSuccessfully() throws IOException, AuthenticationException {
        // given
        ThreadDTO threadToCreateDTO = new ThreadDTO(null, "name", "Name", "Thread description", 1L, "userId");
        ThreadModel threadToCreateModel = new ThreadModel(null, "name", "Name", "Thread description", 1L, "userId");
        ThreadModel createdThreadModel = new ThreadModel(1L, "name", "Name", "Thread description", 1L, "userId");
        ThreadDTO expectedThread = ThreadDTOThreadModel.toDTO(createdThreadModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.CREATED.value(),
                "Thread with name " + createdThreadModel.getName() + " created successfully", expectedThread);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/threads");
        exchange.getIn().setBody(threadToCreateDTO);

        // mock
        when(threadService.createThread(threadToCreateModel)).thenReturn(createdThreadModel);

        // when
        createThreadProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
