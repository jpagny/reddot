package com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.service.KeycloakService;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.thread.UpdateThreadProcessor;
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

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateThreadProcessorTest {

    private UpdateThreadProcessor updateThreadProcessor;

    @Mock
    private ThreadApplicationServiceImpl threadService;

    @Mock
    private KeycloakService keycloakService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        updateThreadProcessor = new UpdateThreadProcessor(threadService, keycloakService);
    }

    @Test
    @DisplayName("given valid thread when updateThread is called then thread updated successfully")
    void givenValidThread_whenUpdateThread_thenThreadIsUpdatedSuccessfully() throws AuthenticationException {
        // given
        Long threadId = 1L;
        ThreadDTO updatedThreadDTO = new ThreadDTO(threadId, "new_name", "New Name", "New Thread description", 1L, "userId");
        ThreadModel updatedThreadModel = new ThreadModel(threadId, "new_name", "New Name", "New Thread description", 1L, "userId");
        ThreadDTO expectedThread = ThreadDTOThreadModel.toDTO(updatedThreadModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "Thread with name " + updatedThreadModel.getName() + " updated successfully", expectedThread);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/threads/" + threadId);
        exchange.getIn().setHeader("id", threadId);
        exchange.getIn().setBody(updatedThreadDTO);

        // mock
        when(threadService.updateThread(threadId, updatedThreadModel)).thenReturn(updatedThreadModel);

        // when
        updateThreadProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }

}
