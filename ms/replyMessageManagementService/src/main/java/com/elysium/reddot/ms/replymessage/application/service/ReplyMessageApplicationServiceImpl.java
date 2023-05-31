package com.elysium.reddot.ms.replymessage.application.service;

import com.elysium.reddot.ms.replymessage.application.exception.type.IsNotOwnerMessageException;
import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceAlreadyExistException;
import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceBadValueException;
import com.elysium.reddot.ms.replymessage.application.exception.type.ResourceNotFoundException;
import com.elysium.reddot.ms.replymessage.domain.constant.ApplicationDefaults;
import com.elysium.reddot.ms.replymessage.domain.exception.DifferentUserException;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;
import com.elysium.reddot.ms.replymessage.domain.port.inbound.IReplyMessageManagementService;
import com.elysium.reddot.ms.replymessage.domain.port.outbound.IReplyMessageRepository;
import com.elysium.reddot.ms.replymessage.domain.service.ReplyMessageDomainServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReplyMessageApplicationServiceImpl implements IReplyMessageManagementService {

    @Value("${message.max.nested.replies:" + ApplicationDefaults.DEFAULT_MAX_NESTED_REPLIES + "}")
    private Integer maxNestedReplies;

    private static final String RESOURCE_NAME_REPLY_MESSAGE = "reply message";
    private final ReplyMessageDomainServiceImpl replyMessageDomainService;
    private final IReplyMessageRepository replyMessageRepository;


    /**
     * {@inheritDoc}
     */
    @Override
    public ReplyMessageModel getReplyMessageById(Long id) {
        log.debug("Fetching replyMessage with id {}", id);

        ReplyMessageModel foundReplyMessageModel = replyMessageRepository.findReplyMessageById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME_REPLY_MESSAGE, String.valueOf(id))
        );

        log.info("Successfully retrieved replyMessage with id {}, name '{}'",
                id, foundReplyMessageModel.getContent());

        return foundReplyMessageModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReplyMessageModel> getAllRepliesMessage() {
        log.info("Fetching all replyMessages from database...");

        return replyMessageRepository.findAllRepliesMessage()
                .parallelStream()
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReplyMessageModel createReplyMessage(ReplyMessageModel replyMessageToCreateModel) {

        log.debug("Creating new replyMessage with content '{}'",
                replyMessageToCreateModel.getContent());

        Optional<ReplyMessageModel> existingReplyMessage = replyMessageRepository
                .findFirstByContentAndParentMessageId(replyMessageToCreateModel.getContent(),
                        replyMessageToCreateModel.getParentMessageID());

        if (existingReplyMessage.isPresent()) {
            throw new ResourceAlreadyExistException(RESOURCE_NAME_REPLY_MESSAGE, "content",
                    replyMessageToCreateModel.getContent(), replyMessageToCreateModel.getParentMessageID());
        }

        replyMessageToCreateModel.setCreatedAt(LocalDateTime.now());
        replyMessageToCreateModel.setUpdatedAt(replyMessageToCreateModel.getCreatedAt());

        try {
            // check validate replyMessage
            replyMessageDomainService.validateReplyMessageForCreation(replyMessageToCreateModel);

            // check nested replies limit
            int countTotalRepliedForThisMessage = replyMessageRepository
                    .countRepliesByMessageId(replyMessageToCreateModel.getParentMessageID());
            log.debug("count total replies : " + countTotalRepliedForThisMessage);
            replyMessageDomainService.verifyNestedRepliesLimit(countTotalRepliedForThisMessage, maxNestedReplies);

            // other check ...

        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_REPLY_MESSAGE, exception.getMessage());

        }

        ReplyMessageModel createdReplyMessageModel = replyMessageRepository.createReplyMessage(replyMessageToCreateModel);

        log.info("Successfully created replyMessage with id {}, content '{}'",
                createdReplyMessageModel.getId(),
                createdReplyMessageModel.getContent());

        return createdReplyMessageModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReplyMessageModel updateReplyMessage(Long id, ReplyMessageModel replyMessageToUpdateModel) {

        log.debug("Updating replyMessage with id '{}', content '{}'",
                id, replyMessageToUpdateModel.getContent());

        Optional<ReplyMessageModel> existingReplyMessageModel = replyMessageRepository.findReplyMessageById(id);

        if (existingReplyMessageModel.isEmpty()) {
            throw new ResourceNotFoundException(RESOURCE_NAME_REPLY_MESSAGE, String.valueOf(id));
        }

        try {
            replyMessageDomainService.validateReplyMessageForUpdate(replyMessageToUpdateModel, existingReplyMessageModel.get());

        } catch (DifferentUserException exception) {
            log.error(exception.getMessage());
            throw new IsNotOwnerMessageException();

        } catch (Exception exception) {
            throw new ResourceBadValueException(RESOURCE_NAME_REPLY_MESSAGE, exception.getMessage());

        }

        replyMessageToUpdateModel.setMessageId(existingReplyMessageModel.get().getParentMessageID());
        replyMessageToUpdateModel.setCreatedAt(existingReplyMessageModel.get().getCreatedAt());
        replyMessageToUpdateModel.setUpdatedAt(LocalDateTime.now());

        ReplyMessageModel updatedReplyMessageModel = replyMessageRepository.updateReplyMessage(replyMessageToUpdateModel);

        log.info("Successfully updated replyMessage with id '{}', content'{}",
                updatedReplyMessageModel.getId(),
                updatedReplyMessageModel.getContent());

        return updatedReplyMessageModel;
    }


}