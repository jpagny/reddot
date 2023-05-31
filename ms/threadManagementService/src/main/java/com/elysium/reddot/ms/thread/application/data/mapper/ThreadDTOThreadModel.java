package com.elysium.reddot.ms.thread.application.data.mapper;

import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The ThreadDTOThreadModel class provides static methods to map between ThreadModel and ThreadDTO objects.
 * It converts a ThreadModel object to a ThreadDTO object and vice versa.
 */
public class ThreadDTOThreadModel {

    private ThreadDTOThreadModel() {
    }

    /**
     * Converts a ThreadModel object to a ThreadDTO object.
     *
     * @param threadModel The ThreadModel object to be converted.
     * @return The corresponding ThreadDTO object.
     */
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

    /**
     * Converts a list of ThreadModel objects to a list of ThreadDTO objects.
     *
     * @param threadModels The list of ThreadModel objects to be converted.
     * @return The corresponding list of ThreadDTO objects.
     */
    public static List<ThreadDTO> toDTOList(List<ThreadModel> threadModels) {
        return threadModels.stream()
                .map(ThreadDTOThreadModel::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a ThreadDTO object to a ThreadModel object.
     *
     * @param threadDTO The ThreadDTO object to be converted.
     * @return The corresponding ThreadModel object.
     */
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
