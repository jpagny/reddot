package com.elysium.reddot.ms.thread.application.service;

import com.elysium.reddot.ms.thread.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.thread.application.exception.type.ResourceNotFoundException;
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

@Service
@Transactional
@Slf4j
public class ThreadApplicationServiceImpl implements IThreadManagementService {

    private static final String RESOURCE_NAME_TOPIC = "thread";
    private final ThreadDomainServiceImpl threadDomainService;
    private final IThreadRepository threadRepository;

    @Autowired
    public ThreadApplicationServiceImpl(IThreadRepository threadRepository) {
        this.threadDomainService = new ThreadDomainServiceImpl();
        this.threadRepository = threadRepository;
    }

    @Override
    public ThreadModel getThreadById(Long id) {
        log.debug("Fetching thread with id {}", id);

        ThreadModel foundThreadModel = threadRepository.findThreadById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_TOPIC, String.valueOf(id))
        );

        log.info("Successfully retrieved thread with id {}, name '{}', description '{}'",
                id, foundThreadModel.getName(), foundThreadModel.getDescription());

        return foundThreadModel;
    }

    @Override
    public List<ThreadModel> getAllThreads() {
        log.info("Fetching all threads from database...");

        return threadRepository.findAllThreads()
                .parallelStream()
                .collect(Collectors.toList());
    }

    @Override
    public ThreadModel createThread(ThreadModel threadToCreateModel) {

        log.debug("Creating new thread with name '{}', label '{}, description '{}'",
                threadToCreateModel.getName(),
                threadToCreateModel.getLabel(),
                threadToCreateModel.getDescription());

        Optional<ThreadModel> existingThread = threadRepository.findThreadByName(threadToCreateModel.getName());

        if (existingThread.isPresent()) {
            throw new ResourceAlreadyExistException(RESOURCE_NAME_TOPIC, "name", threadToCreateModel.getName());
        }

        try {
            threadDomainService.validateThreadForCreation(threadToCreateModel);
        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_TOPIC, exception.getMessage());
        }

        ThreadModel createdThreadModel = threadRepository.createThread(threadToCreateModel);

        log.info("Successfully created thread with id {}, name '{}', label '{}' description '{}'",
                createdThreadModel.getId(),
                createdThreadModel.getName(),
                createdThreadModel.getLabel(),
                createdThreadModel.getDescription());

        return createdThreadModel;
    }

    @Override
    public ThreadModel updateThread(Long id, ThreadModel threadToUpdateModel) {
        log.debug("Updating thread with id '{}', name '{}', label '{}', description '{}'",
                id, threadToUpdateModel.getName(), threadToUpdateModel.getLabel(), threadToUpdateModel.getDescription());

        ThreadModel existingThreadModel = threadRepository.findThreadById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_TOPIC, String.valueOf(id))
        );

        try {
            ThreadModel threadModelWithUpdates = threadDomainService.updateExistingThreadWithUpdates(existingThreadModel, threadToUpdateModel);

            ThreadModel updatedThreadModel = threadRepository.updateThread(threadModelWithUpdates);

            log.info("Successfully updated thread with id '{}', name '{}', label'{}, description '{}'",
                    updatedThreadModel.getId(),
                    updatedThreadModel.getName(),
                    updatedThreadModel.getLabel(),
                    updatedThreadModel.getDescription());

            return updatedThreadModel;

        } catch (Exception ex) {
            throw new ResourceBadValueException(RESOURCE_NAME_TOPIC, ex.getMessage());

        }
    }

    @Override
    public ThreadModel deleteThreadById(Long id) throws ResourceNotFoundException {
        log.debug("Deleting thread with id {}", id);

        ThreadModel threadToDelete = threadRepository.findThreadById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_TOPIC, String.valueOf(id))
        );

        threadRepository.deleteThread(id);

        log.info("Successfully deleted thread with id '{}', name '{}', description '{}'",
                threadToDelete.getId(), threadToDelete.getName(), threadToDelete.getDescription());

        return threadToDelete;
    }

    @Override
    public boolean checkThreadIdExists(Long id) {
        Optional<ThreadModel> threadModel = threadRepository.findThreadById(id);
        return threadModel.isPresent();
    }

}