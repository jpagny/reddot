package com.elysium.reddot.ms.thread.application.data;

import com.elysium.reddot.ms.thread.application.data.dto.ThreadDTO;
import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import com.elysium.reddot.ms.thread.infrastructure.mapper.ThreadProcessorMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadApplicationMapperTest {

    @Test
    @DisplayName("given threadModel when toDTO is called then returns threadDTO")
    void givenThreadModel_whenToDTO_thenThreadDTO() {
        // given
        ThreadModel threadModel = new ThreadModel(1L, "Test Name", "Test Label", "Test Description",1L,"userId");

        // when
        ThreadDTO threadDTO = ThreadProcessorMapper.toDTO(threadModel);

        // then
        assertEquals(threadModel.getId(), threadDTO.getId(), "The thread ID should match");
        assertEquals(threadModel.getName(), threadDTO.getName(), "The thread name should match");
        assertEquals(threadModel.getLabel(), threadDTO.getLabel(), "The thread label should match");
        assertEquals(threadModel.getDescription(), threadDTO.getDescription(), "The thread description should match");
        assertEquals(threadModel.getBoardId(),threadDTO.getBoardId(),"The boardId should match");
        assertEquals(threadModel.getUserId(),threadDTO.getUserId(),"The userId should match");
    }

    @Test
    @DisplayName("given threadDTO when toModel is called then returns threadModel")
    void givenThreadDTO_whenToModel_thenThreadModel() {
        // given
        ThreadDTO threadDTO = new ThreadDTO(1L, "Test Name", "Test Label", "Test Description",1l,"userID");

        // when
        ThreadModel threadModel = ThreadProcessorMapper.toModel(threadDTO);

        // then
        assertEquals(threadDTO.getId(), threadModel.getId(), "The thread ID should match");
        assertEquals(threadDTO.getName(), threadModel.getName(), "The thread name should match");
        assertEquals(threadDTO.getLabel(), threadModel.getLabel(), "The thread label should match");
        assertEquals(threadDTO.getDescription(), threadModel.getDescription(), "The thread description should match");
        assertEquals(threadDTO.getBoardId(),threadModel.getBoardId(),"The boardId should match");
        assertEquals(threadDTO.getUserId(),threadModel.getUserId(),"The userId should match");
    }


}
