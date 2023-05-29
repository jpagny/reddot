package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.thread.GetThreadByIdProcessor;
import com.elysium.reddot.ms.thread.infrastructure.mapper.ThreadDTOThreadModel;
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
class GetThreadByIdProcessorTest {

    private GetThreadByIdProcessor getThreadByIdProcessor;

    @Mock
    private ThreadApplicationServiceImpl threadService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        getThreadByIdProcessor = new GetThreadByIdProcessor(threadService);
    }

    @Test
    @DisplayName("given thread exists when getThreadById is called then thread retrieved")
    void givenThreadExists_whenGetThreadById_thenThreadIsRetrieved() {
        // given
        Long id = 1L;
        ThreadModel threadModel = new ThreadModel(id, "name 1", "Name 1", "Thread 1",1L,"userId");
        ThreadDTO expectedThread = ThreadDTOThreadModel.toDTO(threadModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Thread with id " + id + " retrieved successfully", expectedThread);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/threads/" + id);
        exchange.getIn().setHeader("id", id);

        // mock
        when(threadService.getThreadById(id)).thenReturn(threadModel);

        // when
        getThreadByIdProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }

}
