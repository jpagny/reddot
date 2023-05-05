package com.elysium.reddot.ms.thread.infrastructure.mapper;

import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;

import java.util.List;
import java.util.stream.Collectors;

public class ThreadProcessorMapper {

    private ThreadProcessorMapper() {
    }

    public static ThreadDTO toDTO(ThreadModel threadModel) {
        ThreadDTO threadDTO = new ThreadDTO();
        threadDTO.setId(threadModel.getId());
        threadDTO.setLabel(threadModel.getLabel());
        threadDTO.setName(threadModel.getName());
        threadDTO.setDescription(threadModel.getDescription());
        threadDTO.setBoardId(threadModel.getBoardId());
        threadDTO.setUserId(threadModel.getUserId());
        return threadDTO;
    }

    public static List<ThreadDTO> toDTOList(List<ThreadModel> threadModels) {
        return threadModels.stream()
                .map(ThreadProcessorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static ThreadModel toModel(ThreadDTO threadDTO) {
        return new ThreadModel(
                threadDTO.getId(),
                threadDTO.getName(),
                threadDTO.getLabel(),
                threadDTO.getDescription(),
                threadDTO.getBoardId(),
                threadDTO.getUserId()
        );
    }

}
