package com.elysium.reddot.ms.thread.infrastructure.rest.processor;

import com.elysium.reddot.ms.thread.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.application.service.ThreadApplicationServiceImpl;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.inbound.rest.processor.GetAllThreadsProcessor;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllThreadsProcessorTest {

    private GetAllThreadsProcessor getAllThreadsProcessor;

    @Mock
    private ThreadApplicationServiceImpl threadService;

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        getAllThreadsProcessor = new GetAllThreadsProcessor(threadService);
    }

    @Test
    @DisplayName("given threads exist when get allThreads is called then all threads retrieved")
    void givenThreadsExist_whenGetAllThreads_thenAllThreadsAreRetrieved() {
        // given
        ThreadModel thread1Model = new ThreadModel(1L, "name 1", "Name 1", "Thread 1",1L,"userId");
        ThreadModel thread2Model = new ThreadModel(2L, "name 2", "Name 2", "Thread 2",1L,"userId");
        List<ThreadModel> threadListModel = Arrays.asList(thread1Model, thread2Model);
        List<ThreadDTO> expectedListThreads = ThreadProcessorMapper.toDTOList(threadListModel);

        ApiResponseDTO expectedApiResponse = new ApiResponseDTO(HttpStatus.OK.value(),
                "All threads retrieved successfully", expectedListThreads);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("CamelHttpUri", "/threads");

        // mock
        when(threadService.getAllThreads()).thenReturn(threadListModel);

        // when
        getAllThreadsProcessor.process(exchange);
        ApiResponseDTO actualApiResponse = exchange.getMessage().getBody(ApiResponseDTO.class);

        // then
        assertEquals(expectedApiResponse.getStatus(), actualApiResponse.getStatus());
        assertEquals(expectedApiResponse.getMessage(), actualApiResponse.getMessage());
        assertEquals(expectedApiResponse.getData(), actualApiResponse.getData());
    }


}
