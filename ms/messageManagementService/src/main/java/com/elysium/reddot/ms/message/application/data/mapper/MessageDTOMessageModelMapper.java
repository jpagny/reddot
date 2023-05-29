package com.elysium.reddot.ms.message.application.data.mapper;

import com.elysium.reddot.ms.message.application.data.dto.MessageDTO;
import com.elysium.reddot.ms.message.domain.model.MessageModel;

import java.util.List;
import java.util.stream.Collectors;

public class MessageDTOMessageModelMapper {

    private MessageDTOMessageModelMapper() {
    }

    public static MessageDTO toDTO(MessageModel messageModel) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(messageModel.getId());
        messageDTO.setContent(messageModel.getContent());
        messageDTO.setThreadId(messageModel.getThreadId());
        messageDTO.setUserId(messageModel.getUserId());
        messageDTO.setCreatedAt(messageModel.getCreatedAt());
        messageDTO.setUpdatedAt(messageModel.getUpdatedAt());
        return messageDTO;
    }

    public static List<MessageDTO> toDTOList(List<MessageModel> messageModels) {
        return messageModels.stream()
                .map(MessageDTOMessageModelMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static MessageModel toModel(MessageDTO messageDTO) {
        return new MessageModel(
                messageDTO.getId(),
                messageDTO.getContent(),
                messageDTO.getThreadId(),
                messageDTO.getUserId(),
                messageDTO.getCreatedAt(),
                messageDTO.getUpdatedAt()
        );
    }

}
