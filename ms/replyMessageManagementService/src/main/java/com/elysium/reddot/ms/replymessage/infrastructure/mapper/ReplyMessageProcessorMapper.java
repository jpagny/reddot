package com.elysium.reddot.ms.replymessage.infrastructure.mapper;


import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;

import java.util.List;
import java.util.stream.Collectors;

public class ReplyMessageProcessorMapper {

    private ReplyMessageProcessorMapper() {
    }

    public static ReplyMessageDTO toDTO(ReplyMessageModel replyMessageModel) {
        ReplyMessageDTO replyMessageDTO = new ReplyMessageDTO();
        replyMessageDTO.setId(replyMessageModel.getId());
        replyMessageDTO.setContent(replyMessageModel.getContent());
        replyMessageDTO.setParentMessageID(replyMessageModel.getParentMessageID());
        replyMessageDTO.setUserId(replyMessageModel.getUserId());
        return replyMessageDTO;
    }

    public static List<ReplyMessageDTO> toDTOList(List<ReplyMessageModel> replyMessageModels) {
        return replyMessageModels.stream()
                .map(ReplyMessageProcessorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static ReplyMessageModel toModel(ReplyMessageDTO replyMessageDTO) {
        return new ReplyMessageModel(
                replyMessageDTO.getId(),
                replyMessageDTO.getContent(),
                replyMessageDTO.getParentMessageID(),
                replyMessageDTO.getUserId()
        );
    }

}
