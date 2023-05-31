package com.elysium.reddot.ms.thread.application.service;

import com.elysium.reddot.ms.thread.application.exception.type.IsNotOwnerMessageException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.outbound.persistence.ThreadRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThreadApplicationServiceTest {

    private ThreadApplicationServiceImpl threadService;
    @Mock
    private ThreadRepositoryAdapter threadRepository;

    @BeforeEach
    void setUp() {
        threadService = new ThreadApplicationServiceImpl(threadRepository);
    }

    @Test
    @DisplayName("given valid id when getThreadById is called then returns thread")
    void givenValidId_whenGetThreadById_thenReturnsThread() {
        // given
        Long validId = 1L;
        ThreadModel expectedThread = new ThreadModel(1L, "test", "Test Name", "Test Description",1L,"userId");

        // mock
        when(threadRepository.findThreadById(validId)).thenReturn(Optional.of(expectedThread));

        // when
        ThreadModel actualThread = threadService.getThreadById(validId);

        // then
        assertEquals(expectedThread, actualThread);
        verify(threadRepository, times(1)).findThreadById(validId);
    }

    @Test
    @DisplayName("given invalid id when getThreadById is called then throws ResourceNotFoundException")
    void givenInvalidId_whenGetThreadById_thenThrowsResourceNotFoundException() {
        // given
        Long invalidId = 99L;

        // mock
        when(threadRepository.findThreadById(invalidId)).thenReturn(Optional.empty());

        // then && when
        assertThrows(ResourceNotFoundException.class,
                () -> threadService.getThreadById(invalidId),
                "getThreadById should throw a ResourceNotFoundException for an invalid ID");
        verify(threadRepository, times(1)).findThreadById(invalidId);
    }

    @Test
    @DisplayName("given existing threads, when getAll, then returns List of threads")
    void givenExistingThreads_whenGetAll_thenReturnsListOfThreads() {
        // given
        ThreadModel thread1Model = new ThreadModel(1L, "test1", "Test Name 1", "Test Description 1",1L,"userId");
        ThreadModel thread2Model = new ThreadModel(2L, "test2", "Test Name 2", "Test Description 2",1L,"userId");

        // mock
        List<ThreadModel> expectedListThreads = Arrays.asList(thread1Model, thread2Model);
        when(threadService.getAllThreads()).thenReturn(expectedListThreads);

        // when
        List<ThreadModel> actualThreads = threadService.getAllThreads();

        // then
        assertEquals(expectedListThreads, actualThreads, "The returned thread list should match the expected thread list");
        verify(threadRepository, times(1)).findAllThreads();
    }

    @Test
    @DisplayName("given valid thread when createThread is called then thread created")
    void givenValidThread_whenCreateThread_thenThreadCreated() {
        // given
        ThreadModel threadToCreateLabel = new ThreadModel(null, "test", "Test Label", "Test Description",1L,"userId");
        ThreadModel expectedThread = new ThreadModel(1L, "test", "Test Label", "Test Description",1L,"userId");

        // mock
        when(threadRepository.findFirstByNameAndBoardId(threadToCreateLabel.getName(),threadToCreateLabel.getBoardId())).thenReturn(Optional.empty());
        when(threadRepository.createThread(threadToCreateLabel)).thenReturn(expectedThread);

        // when
        ThreadModel actualThreadModel = threadService.createThread(threadToCreateLabel);

        // then
        assertEquals(expectedThread, actualThreadModel, "The created thread should match the expected thread");
        verify(threadRepository, times(1)).findFirstByNameAndBoardId(expectedThread.getName(),expectedThread.getBoardId());
        verify(threadRepository, times(1)).createThread(threadToCreateLabel);
    }

    @Test
    @DisplayName("given existing thread when createThread is called then throws ResourceAlreadyExistException")
    void givenExistingThread_whenCreateThread_thenThrowsResourceAlreadyExistException() {
        // given
        ThreadModel existingThreadModel = new ThreadModel(1L, "Test Name", "Test Label", "Test Description",1L,"userId");

        // mock
        when(threadRepository.findFirstByNameAndBoardId(existingThreadModel.getName(),existingThreadModel.getBoardId())).thenReturn(Optional.of(existingThreadModel));

        // when && then
        assertThrows(ResourceAlreadyExistException.class,
                () -> threadService.createThread(existingThreadModel),
                "createThread should throw a ResourceAlreadyExistException for an existing thread");
        verify(threadRepository, times(1)).findFirstByNameAndBoardId(existingThreadModel.getName(),existingThreadModel.getBoardId());
    }

    @Test
    @DisplayName("given invalid thread when createThread is called then throws ResourceBadValueException")
    void givenInvalidThread_whenCreateThread_thenThrowsResourceBadValueException() {
        // given
        ThreadModel invalidThreadModel = new ThreadModel(1L, "", "Invalid Label", "Invalid Description",1L,"userId");

        // mock
        when(threadRepository.findFirstByNameAndBoardId(invalidThreadModel.getName(),invalidThreadModel.getBoardId())).thenReturn(Optional.empty());

        // when && then
        assertThrows(ResourceBadValueException.class,
                () -> threadService.createThread(invalidThreadModel),
                "createThread should throw a ResourceBadValueException for an invalid thread");
        verify(threadRepository, times(1)).findFirstByNameAndBoardId(invalidThreadModel.getName(),invalidThreadModel.getBoardId());
    }

    @Test
    @DisplayName("given validThread when updateThread is called then thread updated")
    void givenValidThread_whenUpdateThread_thenThreadUpdated() {
        // given
        Long threadId = 1L;
        ThreadModel existingThreadModel = new ThreadModel(threadId, "Old Name", "OldLabel", "Old Description",1L,"userId");
        ThreadModel threadToUpdateModel = new ThreadModel(threadId, "Old Name", "NewLabel", "NewDescription",1L,"userId");
        ThreadModel expectedThread = new ThreadModel(threadId, "Old Name", "NewLabel", "NewDescription",1L,"userId");

        // mock
        when(threadRepository.findThreadById(threadId)).thenReturn(Optional.of(existingThreadModel));
        when(threadRepository.updateThread(expectedThread)).thenReturn(expectedThread);

        // when
        ThreadModel actualThreadDTO = threadService.updateThread(1L, threadToUpdateModel);

        // then
        assertEquals(expectedThread, actualThreadDTO, "The updated thread should match the expected thread");
        verify(threadRepository, times(1)).findThreadById(threadId);
        verify(threadRepository, times(1)).updateThread(expectedThread);
    }

    @Test
    @DisplayName("given non-existent thread when updateThread is called then throws ResourceNotFoundException")
    void givenNonExistentThread_whenUpdateThread_thenThrowsResourceNotFoundException() {
        // given
        Long nonExistentThreadId = 99L;
        ThreadModel threadToUpdateModel = new ThreadModel(nonExistentThreadId, "New Name", "New Label", "New Description",1L,"userId");

        // mock
        when(threadRepository.findThreadById(nonExistentThreadId)).thenReturn(Optional.empty());

        // then && when
        assertThrows(ResourceNotFoundException.class,
                () -> threadService.updateThread(nonExistentThreadId, threadToUpdateModel),
                "updateThread should throw a ResourceNotFoundException for a non-existent thread");
        verify(threadRepository, times(1)).findThreadById(nonExistentThreadId);
    }

    @Test
    @DisplayName("given invalid thread when updateThread is called then throws ResourceBadValueException")
    void givenInvalidThread_whenUpdateThread_thenThrowsResourceBadValueException() {
        // given
        Long threadId = 1L;
        ThreadModel existingThreadModel = new ThreadModel(threadId, "Old Name", "Old Label", "Old Description",1L,"userId");
        ThreadModel invalidThreadToUpdateModel = new ThreadModel(threadId, "Invalid Name", "", "Invalid Description",1L,"userId");

        // mock
        when(threadRepository.findThreadById(threadId)).thenReturn(Optional.of(existingThreadModel));

        // when && then
        assertThrows(ResourceBadValueException.class,
                () -> threadService.updateThread
                        (threadId, invalidThreadToUpdateModel),
                "updateThread should throw a ResourceBadValueException for an invalid thread");
        verify(threadRepository, times(1)).findThreadById(threadId);
    }

    @Test
    @DisplayName("given invalid thread when updateThread is called then throws ResourceBadValueException")
    void givenValidThreadWithDifferentUserId_whenUpdateThread_thenThrowsResourceNoOwner() {
        // given
        Long threadId = 1L;
        ThreadModel existingThreadModel = new ThreadModel(threadId, "name", "Old Label", "Old Description",1L,"userId");
        ThreadModel threadWithWrongUserIdToUpdateModel = new ThreadModel(threadId, "name", "New label", "Description",1L,"userId2");

        // mock
        when(threadRepository.findThreadById(threadId)).thenReturn(Optional.of(existingThreadModel));

        // when && then
        assertThrows(IsNotOwnerMessageException.class,
                () -> threadService.updateThread
                        (threadId, threadWithWrongUserIdToUpdateModel),
                "updateThread should throw a IsNotOwnerMessageException for an invalid userId");
        verify(threadRepository, times(1)).findThreadById(threadId);
    }


}
