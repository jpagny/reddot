package com.elysium.reddot.ms.replymessage.application.service;

import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.replymessage.domain.exception.LimitExceededException;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.domain.service.ReplyMessageDomainServiceImpl;
import com.elysium.reddot.ms.replymessage.infrastructure.outbound.persistence.ReplyMessageRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReplyMessageApplicationServiceTest {

    @Mock
    private ReplyMessageDomainServiceImpl replyMessageDomainService;
    @Mock
    private ReplyMessageRepositoryAdapter replyMessageRepository;

    @InjectMocks
    private ReplyMessageApplicationServiceImpl replyMessageApplicationService;


    @Test
    @DisplayName("given valid id when getReplyMessageById then returns ReplyMessageModel")
    void givenValidId_whenGetReplyMessageById_thenReturnReplyMessageModel() {
        // given
        Long id = 1L;
        ReplyMessageModel replyMessageModel = new ReplyMessageModel("content", 1L, "userId");
        replyMessageModel.setContent("Content");

        // mock
        when(replyMessageRepository.findReplyMessageById(id)).thenReturn(Optional.of(replyMessageModel));

        // when
        ReplyMessageModel result = replyMessageApplicationService.getReplyMessageById(id);

        // then
        assertEquals(replyMessageModel, result);
    }

    @Test
    @DisplayName("given non-existent id when getReplyMessageById then throws ResourceNotFoundException")
    void givenNonExistentId_whenGetReplyMessageById_thenThrowsResourceNotFoundException() {
        // given
        Long id = 1L;

        // mock
        when(replyMessageRepository.findReplyMessageById(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> replyMessageApplicationService.getReplyMessageById(id));
    }

    @Test
    @DisplayName("When getAllRepliesMessage then returns list of ReplyMessageModel")
    void whenGetAllRepliesMessage_thenReturnListOfReplyMessageModel() {
        // given
        ReplyMessageModel replyMessageModel1 = new ReplyMessageModel("content", 1L, "userId");
        ReplyMessageModel replyMessageModel2 = new ReplyMessageModel("content2", 1L, "userId");

        // mock
        when(replyMessageRepository.findAllRepliesMessage()).thenReturn(Arrays.asList(replyMessageModel1, replyMessageModel2));

        // when
        List<ReplyMessageModel> result = replyMessageApplicationService.getAllRepliesMessage();

        // then
        assertEquals(Arrays.asList(replyMessageModel1, replyMessageModel2), result);
    }

    @Test
    @DisplayName("given valid ReplyMessageModel when createReplyMessage then returns ReplyMessageModel")
    void givenValidReplyMessageModel_whenCreateReplyMessage_thenReturnReplyMessageModel() {
        // given
        ReplyMessageModel replyMessageToCreateModel = new ReplyMessageModel("content", 1L, "userId");
        ReflectionTestUtils.setField(replyMessageApplicationService, "maxNestedReplies", 10);

        // mock
        when(replyMessageRepository.countRepliesByMessageId(replyMessageToCreateModel.getParentMessageID())).thenReturn(0);
        when(replyMessageRepository.createReplyMessage(replyMessageToCreateModel)).thenReturn(replyMessageToCreateModel);

        // when
        ReplyMessageModel result = replyMessageApplicationService.createReplyMessage(replyMessageToCreateModel);

        // then
        assertEquals(replyMessageToCreateModel, result);
    }

    @Test
    @DisplayName("given valid ReplyMessageModel but exceeds nested reply limit when createReplyMessage then throws ResourceBadValueException")
    void givenValidReplyMessageModelButExceedsLimit_whenCreateReplyMessage_thenThrowsResourceBadValueException() {
        // given
        ReplyMessageModel replyMessageToCreateModel = new ReplyMessageModel("content", 1L, "userId");
        ReflectionTestUtils.setField(replyMessageApplicationService, "maxNestedReplies", 10);

        // mock
        when(replyMessageRepository.countRepliesByMessageId(1L)).thenReturn(11);
        doThrow(new LimitExceededException("Max limit of nested replies reached: " + 10))
                .when(replyMessageDomainService)
                .verifyNestedRepliesLimit(11, 10);

        // when & then
        assertThrows(ResourceBadValueException.class, () -> replyMessageApplicationService.createReplyMessage(replyMessageToCreateModel));
    }

    @Test
    @DisplayName("given valid id and ReplyMessageModel when updateReplyMessage then returns updated ReplyMessageModel")
    void givenValidIdAndReplyMessageModel_whenUpdateReplyMessage_thenReturnUpdatedReplyMessageModel() {
        // given
        Long id = 1L;
        ReplyMessageModel replyMessageToUpdateModel = new ReplyMessageModel("updated content", 1L, "userId");
        ReplyMessageModel existingReplyMessageModel = new ReplyMessageModel("content", 1L, "userId");

        // mock
        when(replyMessageRepository.findReplyMessageById(id)).thenReturn(Optional.of(existingReplyMessageModel));
        when(replyMessageRepository.updateReplyMessage(existingReplyMessageModel)).thenReturn(replyMessageToUpdateModel);

        // when
        ReplyMessageModel result = replyMessageApplicationService.updateReplyMessage(id, replyMessageToUpdateModel);

        // then
        assertEquals(replyMessageToUpdateModel, result);
    }

    @Test
    @DisplayName("given non-existent id when updateReplyMessage then throws ResourceNotFoundException")
    void givenNonExistentId_whenUpdateReplyMessage_thenThrowsResourceNotFoundException() {
        // given
        Long id = 1L;
        ReplyMessageModel replyMessageToUpdateModel = new ReplyMessageModel("updated content", 1L, "userId");

        // mock
        when(replyMessageRepository.findReplyMessageById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(ResourceNotFoundException.class, () -> replyMessageApplicationService.updateReplyMessage(id, replyMessageToUpdateModel));
    }


}
