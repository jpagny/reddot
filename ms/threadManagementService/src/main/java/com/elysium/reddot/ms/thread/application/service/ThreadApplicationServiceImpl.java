package com.elysium.reddot.ms.thread.application.service;

import com.elysium.reddot.ms.thread.application.exception.type.IsNotOwnerMessageException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.thread.domain.exception.type.DifferentUserException;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.domain.port.inbound.IThreadManagementService;
import com.elysium.reddot.ms.thread.domain.port.outbound.IThreadRepository;
import com.elysium.reddot.ms.thread.domain.service.ThreadDomainServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The ThreadApplicationServiceImpl class implements the IThreadManagementService interface
 * and provides the implementation for managing threads in the application layer.
 */
@Service
@Transactional
@Slf4j
public class ThreadApplicationServiceImpl implements IThreadManagementService {

    private static final String RESOURCE_NAME_THREAD = "thread";
    private final ThreadDomainServiceImpl threadDomainService;
    private final IThreadRepository threadRepository;

    @Autowired
    public ThreadApplicationServiceImpl(IThreadRepository threadRepository) {
        this.threadDomainService = new ThreadDomainServiceImpl();
        this.threadRepository = threadRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThreadModel getThreadById(Long id) {
        log.debug("Fetching thread with id {}", id);

        ThreadModel foundThreadModel = threadRepository.findThreadById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_THREAD, String.valueOf(id))
        );

        log.info("Successfully retrieved thread with id {}, name '{}', description '{}'",
                id, foundThreadModel.getName(), foundThreadModel.getDescription());

        return foundThreadModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ThreadModel> getAllThreads() {
        log.info("Fetching all threads from database...");

        return threadRepository.findAllThreads()
                .parallelStream()
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThreadModel createThread(ThreadModel threadToCreateModel) {

        log.debug("Creating new thread with name '{}', label '{}, description '{}', boardId '{}', userId '{}'",
                threadToCreateModel.getName(),
                threadToCreateModel.getLabel(),
                threadToCreateModel.getDescription(),
                threadToCreateModel.getBoardId(),
                threadToCreateModel.getUserId());

        Optional<ThreadModel> existingThread = threadRepository.findFirstByNameAndBoardId(threadToCreateModel.getName(), threadToCreateModel.getBoardId());

        if (existingThread.isPresent()) {
            throw new ResourceAlreadyExistException(RESOURCE_NAME_THREAD, "name", threadToCreateModel.getName(), threadToCreateModel.getBoardId());
        }

        try {
            threadDomainService.validateThreadForCreation(threadToCreateModel);
        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_THREAD, exception.getMessage());
        }

        ThreadModel createdThreadModel = threadRepository.createThread(threadToCreateModel);

        log.debug("Creating new thread with name '{}', label '{}, description '{}', boardId '{}', userId '{}'",
                createdThreadModel.getName(),
                createdThreadModel.getLabel(),
                createdThreadModel.getDescription(),
                createdThreadModel.getBoardId(),
                createdThreadModel.getUserId());


        return createdThreadModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThreadModel updateThread(Long id, ThreadModel threadToUpdateModel) {
        log.debug("Updating new thread with name '{}', label '{}, description '{}', boardId '{}', userId '{}'",
                threadToUpdateModel.getName(),
                threadToUpdateModel.getLabel(),
                threadToUpdateModel.getDescription(),
                threadToUpdateModel.getBoardId(),
                threadToUpdateModel.getUserId());

        ThreadModel existingThreadModel = threadRepository.findThreadById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_THREAD, String.valueOf(id))
        );

        try {
            ThreadModel threadModelWithUpdates = threadDomainService.updateExistingThreadWithUpdates(threadToUpdateModel, existingThreadModel);
            ThreadModel updatedThreadModel = threadRepository.updateThread(threadModelWithUpdates);

            log.info("Successfully updated thread with name '{}', label'{}, description '{}', boardId '{}', userId '{}'",
                    updatedThreadModel.getName(),
                    updatedThreadModel.getLabel(),
                    updatedThreadModel.getDescription(),
                    updatedThreadModel.getBoardId(),
                    updatedThreadModel.getUserId());

            return updatedThreadModel;

        } catch (DifferentUserException exception) {
            log.error(exception.getMessage());
            throw new IsNotOwnerMessageException();

        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_THREAD, exception.getMessage());

        }

    }


}