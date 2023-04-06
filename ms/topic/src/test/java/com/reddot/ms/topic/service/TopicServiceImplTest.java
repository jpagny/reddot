package com.reddot.ms.topic.service;

import com.reddot.ms.topic.data.dto.TopicDTO;
import com.reddot.ms.topic.data.entity.TopicEntity;
import com.reddot.ms.topic.data.mapper.TopicMapper;
import com.reddot.ms.topic.data.repository.TopicRepository;
import com.reddot.ms.topic.exception.ResourceAlreadyExistException;
import com.reddot.ms.topic.exception.ResourceNotFoundException;
import com.reddot.ms.topic.service.implement.TopicServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceImplTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicMapper topicMapper;

    @InjectMocks
    private TopicServiceImpl topicService;


    @Test
    @DisplayName("given existing topic, when getById, then returns topicDTO")
    void givenExistingTopic_whenGetById_thenReturnsTopicDTO() throws ResourceNotFoundException {
        // given
        Long topicId = 1L;
        TopicEntity topicEntity = new TopicEntity(topicId, "test topic", "Test Topic", "This is a test topic");
        TopicDTO expectedTopicDTO = TopicDTO.builder()
                .id(topicId)
                .name("test topic")
                .label("Test Topic")
                .description("This is a test topic")
                .build();

        // mock
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topicEntity));
        when(topicMapper.toDTO(topicEntity)).thenReturn(expectedTopicDTO);

        // when
        TopicDTO actualTopicDTO = topicService.getById(topicId);

        // then
        assertThat(actualTopicDTO).isEqualTo(expectedTopicDTO);
        verify(topicRepository, times(1)).findById(topicId);
        verifyNoMoreInteractions(topicRepository);
        verify(topicMapper, times(1)).toDTO(topicEntity);
        verifyNoMoreInteractions(topicMapper);
    }

    @Test
    @DisplayName("given non existing topic, when getById, then throw ResourceNotFoundException")
    void givenNonExistingTopic_whenGetById_thenThrowResourceNotFoundException() {
        // given
        Long topicId = 1L;

        // mock
        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        // when && then
        assertThrows(ResourceNotFoundException.class, () -> topicService.getById(topicId));

        // then
        verify(topicRepository, times(1)).findById(topicId);
        verifyNoMoreInteractions(topicRepository);
        verifyNoInteractions(topicMapper);
    }

    @Test
    @DisplayName("given existing topics, when getAll, then returns List of topicDTO")
    void givenExistingTopics_whenGetAll_thenReturnsListOfTopicDTOs() {
        // given
        TopicEntity topicEntity1 = new TopicEntity(1L, "topic1", "Topic 1", "Description 1");
        TopicEntity topicEntity2 = new TopicEntity(2L, "topic2", "Topic 2", "Description 2");
        List<TopicEntity> topicEntities = Arrays.asList(topicEntity1, topicEntity2);

        TopicDTO topicDTO1 = TopicDTO.builder().id(1L).name("topic1").label("Topic 1").description("Description 1").build();
        TopicDTO topicDTO2 = TopicDTO.builder().id(2L).name("topic2").label("Topic 2").description("Description 2").build();
        List<TopicDTO> expectedTopicDTOs = Arrays.asList(topicDTO1, topicDTO2);

        // mock
        when(topicRepository.findAll()).thenReturn(topicEntities);
        when(topicMapper.toDTO(topicEntity1)).thenReturn(topicDTO1);
        when(topicMapper.toDTO(topicEntity2)).thenReturn(topicDTO2);

        // when
        List<TopicDTO> actualTopicDTOs = topicService.getAll();

        // then
        assertThat(actualTopicDTOs).isEqualTo(expectedTopicDTOs);
        verify(topicRepository).findAll();
    }

    @Test
    @DisplayName("given new topicDTO, when create topic, then topic created")
    void givenNewTopicDTO_whenCreateTopic_thenTopicCreated() throws ResourceAlreadyExistException {
        // given
        TopicDTO topicDTO = TopicDTO.builder().id(1L).name("Test").label("Test topic").description("This is a test topic").build();

        TopicEntity expectedEntity = new TopicEntity();
        expectedEntity.setName("Test");
        expectedEntity.setLabel("Test topic");
        expectedEntity.setDescription("This is a test topic");

        // mock
        when(topicMapper.toEntity(topicDTO)).thenReturn(expectedEntity);
        when(topicRepository.findByName("Test")).thenReturn(Optional.empty());
        when(topicRepository.save(expectedEntity)).thenReturn(expectedEntity);
        when(topicMapper.toDTO(expectedEntity)).thenReturn(topicDTO);

        // when
        TopicDTO createdTopic = topicService.create(topicDTO);

        // then
        assertThat(createdTopic.getId()).isNotNull();
        assertThat(createdTopic.getName()).isEqualTo("Test");
        assertThat(createdTopic.getLabel()).isEqualTo("Test topic");
        assertThat(createdTopic.getDescription()).isEqualTo("This is a test topic");
    }

    @Test
    @DisplayName("given an existing topic when creating a new topic with the same name then it should throw a ResourceAlreadyExistException")
    void createExistingTopic_withSameName_shouldThrowResourceAlreadyExistException() {
        // given
        TopicDTO topicDTO = TopicDTO.builder().id(1L).name("Test").label("Test topic").description("This is a test topic").build();

        TopicEntity expectedEntity = new TopicEntity();
        expectedEntity.setName("Test");
        expectedEntity.setLabel("Test topic");
        expectedEntity.setDescription("This is a test topic");

        // mock
        when(topicMapper.toEntity(topicDTO)).thenReturn(expectedEntity);
        when(topicRepository.findByName("Test")).thenReturn(Optional.of(expectedEntity));

        // when && then
        assertThrows(ResourceAlreadyExistException.class, () -> topicService.create(topicDTO));
    }

    @Test
    @DisplayName("given existing topic when update topic then topicDTO with new values returned")
    void givenExistingTopic_whenUpdateTopic_thenTopicDTOWithNewValuesReturned() throws ResourceNotFoundException {
        // given
        TopicDTO existingTopicTDO = TopicDTO.builder().id(1L).name("Test").label("Test topic").description("This is a test topic").build();
        TopicEntity existingTopicEntity = new TopicEntity(1L, "Test", "Test topic", "This is a test topic");

        String updatedName = "updatedName";
        String updatedDescription = "updated description";
        TopicDTO updatedTopicDTO = TopicDTO.builder()
                .id(existingTopicTDO.getId())
                .name(updatedName)
                .label(existingTopicTDO.getLabel())
                .description(updatedDescription)
                .build();

        TopicEntity expectedEntity = new TopicEntity(1L, "Test", "Test topic", "This is a test topic");

        // mock
        when(topicRepository.findById(existingTopicTDO.getId())).thenReturn(Optional.of(existingTopicEntity));
        when(topicMapper.toEntity(updatedTopicDTO)).thenReturn(expectedEntity);
        when(topicRepository.save(expectedEntity)).thenReturn(expectedEntity);
        when(topicMapper.toDTO(expectedEntity)).thenReturn(updatedTopicDTO);

        // when
        TopicDTO result = topicService.update(1L, updatedTopicDTO);

        // then
        verify(topicRepository).findById(existingTopicTDO.getId());
        verify(topicRepository).save(any(TopicEntity.class));
        assertThat(result.getId()).isEqualTo(existingTopicTDO.getId());
        assertThat(result.getName()).isEqualTo(updatedName);
        assertThat(result.getDescription()).isEqualTo(updatedDescription);
    }

    @Test
    @DisplayName("given non existing topic, when update topic, then throw resource NotFoundException")
    void givenNonExistingTopic_whenUpdateTopic_thenThrowResourceNotFoundException() {
        // given
        Long nonExistingId = 1L;
        TopicDTO nonExistingTopic = TopicDTO.builder()
                .id(nonExistingId)
                .name("nonExistingTopic")
                .label("Non-existing Topic")
                .description("Description of the non-existing topic")
                .build();

        // mock
        when(topicRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> topicService.update(1L, nonExistingTopic))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("The topic with ID " + nonExistingTopic.getId() + " does not exist.");
    }

    @Test
    @DisplayName("given existing topic, when deleting topic, then no exception is thrown")
    void givenExistingTopic_whenDeletingTopic_thenNoExceptionIsThrown() throws ResourceNotFoundException {
        // given
        Long id = 1L;
        TopicEntity topicToDelete = new TopicEntity(id, "test", "Test", "Test topic");

        // mock
        when(topicRepository.findById(id)).thenReturn(Optional.of(topicToDelete));

        // when
        topicService.delete(id);

        // then
        verify(topicRepository, times(1)).delete(topicToDelete);
    }

    @Test
    @DisplayName("given non existing topic, when deleting topic, then throws ResourceNotFoundException")
    void givenNonExistingTopic_whenDeletingTopic_thenThrowsResourceNotFoundException() {
        // given
        Long id = 1L;

        // mock
        when(topicRepository.findById(id)).thenReturn(Optional.empty());

        // when && then
        assertThrows(ResourceNotFoundException.class, () -> topicService.delete(id));
    }

}
