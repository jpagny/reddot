package com.elysium.reddot.ms.replymessage.infrastructure.mapper;


import com.elysium.reddot.ms.replymessage.application.data.dto.ReplyMessageDTO;
import com.elysium.reddot.ms.replymessage.domain.model.ReplyMessageModel;

import java.util.List;
import java.util.stream.Collectors;

public class ReplyMessageModelReplyMessageDTOMapper {

    private ReplyMessageModelReplyMessageDTOMapper() {
    }

    /**
     * Maps a ReplyMessageModel to a ReplyMessageDTO.
     *
     * @param replyMessageModel the ReplyMessageModel to be mapped
     * @return the mapped ReplyMessageDTO
     */
    public static ReplyMessageDTO toDTO(ReplyMessageModel replyMessageModel) {
        ReplyMessageDTO replyMessageDTO = new ReplyMessageDTO();
        replyMessageDTO.setId(replyMessageModel.getId());
        replyMessageDTO.setContent(replyMessageModel.getContent());
        replyMessageDTO.setParentMessageID(replyMessageModel.getParentMessageID());
        replyMessageDTO.setUserId(replyMessageModel.getUserId());
        replyMessageDTO.setCreatedAt(replyMessageModel.getCreatedAt());
        replyMessageDTO.setUpdatedAt(replyMessageModel.getUpdatedAt());
        return replyMessageDTO;
    }

    /**
     * Maps a list of ReplyMessageModel to a list of ReplyMessageDTO.
     *
     * @param replyMessageModels the list of ReplyMessageModel to be mapped
     * @return the mapped list of ReplyMessageDTO
     */
    public static List<ReplyMessageDTO> toDTOList(List<ReplyMessageModel> replyMessageModels) {
        return replyMessageModels.stream()
                .map(ReplyMessageModelReplyMessageDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps a ReplyMessageDTO to a ReplyMessageModel.
     *
     * @param replyMessageDTO the ReplyMessageDTO to be mapped
     * @return the mapped ReplyMessageModel
     */
    public static ReplyMessageModel toModel(ReplyMessageDTO replyMessageDTO) {
        return new ReplyMessageModel(
                replyMessageDTO.getId(),
                replyMessageDTO.getContent(),
                replyMessageDTO.getParentMessageID(),
                replyMessageDTO.getUserId(),
                replyMessageDTO.getCreatedAt(),
                replyMessageDTO.getUpdatedAt()
        );
    }

}
