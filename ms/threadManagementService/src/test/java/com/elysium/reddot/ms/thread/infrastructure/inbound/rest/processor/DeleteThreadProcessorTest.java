package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.thread.DeleteThreadProcessor;
import com.elysium.reddot.ms.thread.infrastructure.mapper.ThreadProcessorMapper;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteThreadProcessorTest {

    private DeleteThreadProcessor deleteThreadProcessor;

    @Mock
    private ThreadApplicationServiceImpl threadService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        deleteThreadProcessor = new DeleteThreadProcessor(threadService);
    }

    @Test
    @DisplayName("given valid threadId when deleteThread is called then thread deleted Successfully")
    void givenValidThreadId_whenDeleteThread_thenThreadIsDeletedSuccessfully() {
        // given
        Long threadId = 1L;
        ThreadModel threadToDeleteModel = new ThreadModel(threadId, "name", "Name", "Thread description",1L,"userId");
        ThreadDTO expectedThread = ThreadProcessorMapper.toDTO(threadToDeleteModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Thread with id " + threadId + " deleted successfully", expectedThread);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/threads/" + threadId);
        exchange.getIn().setHeader("id", threadId);

        // mock
        when(threadService.deleteThreadById(threadId)).thenReturn(threadToDeleteModel);

        // when
        deleteThreadProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());

        // verify
        verify(threadService).deleteThreadById(threadId);
    }

}
