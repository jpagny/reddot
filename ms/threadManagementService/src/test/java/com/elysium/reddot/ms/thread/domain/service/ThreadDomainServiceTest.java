package com.elysium.reddot.ms.thread.domain.service;

import com.elysium.reddot.ms.thread.domain.model.ThreadModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreadDomainServiceTest {

    private static ThreadDomainServiceImpl threadDomainService;

    @BeforeAll
    static void setUp() {
        threadDomainService = new ThreadDomainServiceImpl();
    }

    @Test
    @DisplayName("given threadModel with null name= when validateThreadForCreation is called then throws FieldEmptyException")
    void givenThreadModelWithNullName_whenValidateBuildThread_thenThrowsFieldEmptyException() {
        // given
        ThreadModel threadModel = new ThreadModel(1L, null, "test", "description", 1L, "userId");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> threadDomainService.validateThreadForCreation(threadModel));
    }

    @Test
    @DisplayName("given threadModel with blank name when validateThreadForCreation is called then throws FieldEmptyException")
    void givenThreadModelWithBlankName_whenValidateBuildThread_thenThrowsFieldEmptyException() {
        // given
        ThreadModel threadModel = new ThreadModel(1L, "", "test", "description", 1L, "userId");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> threadDomainService.validateThreadForCreation(threadModel));
    }

    @Test
    @DisplayName("given threadModel with space name when validateThreadForCreation is called then throws FieldWithSpaceException")
    void givenThreadModelWithSpaceName_whenValidateBuildThread_thenThrowsFieldWithSpaceException() {
        // given
        ThreadModel threadModel = new ThreadModel(1L, "name 1", "test", "description", 1L, "userId");

        // then && throw
        assertThrows(FieldWithSpaceException.class, () -> threadDomainService.validateThreadForCreation(threadModel));
    }

    @Test
    @DisplayName("given threadModel with null label when validateThreadForCreation is called then throws FieldEmptyException")
    void givenThreadModelWithNullLabel_whenValidateBuildThread_thenThrowsFieldEmptyException() {
        // given
        ThreadModel threadModel = new ThreadModel(1L, "test", null, "description", 1L, "userId");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> threadDomainService.validateThreadForCreation(threadModel));
    }

    @Test
    @DisplayName("given threadModelWith blank label when validateThreadForCreation is called then throws FieldEmptyException")
    void givenThreadModelWithBlankLabel_whenValidateBuildThread_thenThrowsFieldEmptyException() {
        // given
        ThreadModel threadModel = new ThreadModel(1L, "test", " ", "description", 1L, "userId");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> threadDomainService.validateThreadForCreation(threadModel));
    }

    @Test
    @DisplayName("given valid threadModel when validateThreadForCreation is called then no exception thrown")
    void givenValidThreadModel_whenValidateBuildThread_thenNoExceptionThrown() {
        ThreadModel threadModel = new ThreadModel(1L, "test", "test", "description", 1L, "userId");

        assertDoesNotThrow(() -> threadDomainService.validateThreadForCreation(threadModel),
                "validateBuildThread should not throw an exception for a valid ThreadModel");
    }

    @Test
    @DisplayName("given existing thread and thread to update when updateExistingThreadWithUpdates is called then thread updated successfully")
    void givenExistingThreadAndThreadToUpdate_whenUpdateThread_thenThreadUpdatedSuccessfully() {
        // given
        ThreadModel existingThread = new ThreadModel(1L, "test", "test", "description", 1L, "userId");
        ThreadModel threadToUpdate = new ThreadModel(existingThread.getId(),
                existingThread.getName(),
                existingThread.getLabel(),
                existingThread.getDescription(),
                existingThread.getBoardId(),
                existingThread.getUserId());
        threadToUpdate.setLabel("Updated Label");
        threadToUpdate.setDescription("Updated Description");

        // when
        ThreadModel updatedThread = threadDomainService.updateExistingThreadWithUpdates(existingThread, threadToUpdate);

        // then
        assertEquals("Updated Label", updatedThread.getLabel());
        assertEquals("Updated Description", updatedThread.getDescription());
        assertEquals("test", updatedThread.getName());
    }

    @Test
    @DisplayName("given threadModel to update with blank label when validateThreadForCreation is called then throws FieldEmptyException")
    void givenThreadModelToUpdateWithBlankLabel_whenValidateUpdateThread_thenThrowsFieldEmptyException() {
        // given
        ThreadModel existingThread = new ThreadModel(1L, "test", "test", "description", 1L, "userId");
        ThreadModel threadToUpdate = new ThreadModel(existingThread.getId(),
                existingThread.getName(),
                existingThread.getLabel(),
                existingThread.getDescription(),
                existingThread.getBoardId(),
                existingThread.getUserId());
        threadToUpdate.setLabel("");
        threadToUpdate.setDescription("Updated Description");

        // then && throw
        assertThrows(FieldEmptyException.class, () -> threadDomainService.validateThreadForUpdate(threadToUpdate));
    }

    @Test
    @DisplayName("test hash code")
    void testHashCode() {
        ThreadModel thread1 = new ThreadModel(1L, "name1", "label1", "description1", 1L, "userId");
        ThreadModel thread2 = new ThreadModel(1L, "name2", "label2", "description2", 1L, "userId");
        ThreadModel thread3 = new ThreadModel(2L, "name1", "label1", "description1", 1L, "userId");
        ThreadModel thread4 = new ThreadModel(null, "name1", "label1", "description1", 1L, "userId");

        assertEquals(thread1.hashCode(), thread2.hashCode());
        assertNotEquals(thread1.hashCode(), thread3.hashCode());
        assertEquals(0, thread4.hashCode());
    }

    @Test
    @DisplayName("test thread equal")
    void testAreThreadsEqual() {
        ThreadModel thread1 = new ThreadModel(1L, "name1", "label1", "description1", 1L, "userId");
        ThreadModel thread2 = new ThreadModel(1L, "name2", "label2", "description2", 1L, "userId");
        ThreadModel thread3 = new ThreadModel(2L, "name3", "label3", "description3", 1L, "userId");
        Object otherObject = new Object();

        assertEquals(thread1, thread1);
        assertEquals(thread1, thread2);
        assertNotEquals(thread1, thread3);
        assertNotEquals(thread1, otherObject);
        assertNotEquals(null, thread1);
    }

    @Test
    @DisplayName("test to string")
    void testToString() {
        ThreadModel thread = new ThreadModel(1L, "name1", "label1", "description1", 1L, "userId");

        String expectedToString = "Thread{" +
                "id=" + 1L +
                ", name='name1'" +
                ", label='label1'" +
                ", description='description1'" +
                ", boardId='1'" +
                ", userId='userId'" +
                '}';

        assertEquals(expectedToString, thread.toString());
    }

}
