package com.taskmate.service.impl;

import com.taskmate.entity.Task;
import com.taskmate.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task testTask;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testTask = new Task("Test Task", "Test Description", LocalDateTime.now().plusDays(1));
        testTask.markCompleted();
    }

    @Test
    @DisplayName("Should create task successfully")
    void shouldCreateTaskSuccessfully() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task createdTask = taskService.createTask(testTask);

        assertNotNull(createdTask);
        assertEquals(testTask.getTitle(), createdTask.getTitle());
        assertEquals(testTask.getDescription(), createdTask.getDescription());
        verify(taskRepository, times(1)).save(testTask);
    }

    @Test
    @DisplayName("Should get all tasks")
    void shouldGetAllTasks() {
        List<Task> tasks = Arrays.asList(testTask);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals(testTask.getTitle(), result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get task by ID when found")
    void shouldGetTaskByIdWhenFound() {
        when(taskRepository.findById(testId)).thenReturn(Optional.of(testTask));

        Task result = taskService.getTaskById(testId.toString());

        assertNotNull(result);
        assertEquals(testTask.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).findById(testId);
    }

    @Test
    @DisplayName("Should return null when task not found by ID")
    void shouldReturnNullWhenTaskNotFoundById() {
        UUID nonExistentId = UUID.randomUUID();
        when(taskRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Task result = taskService.getTaskById(nonExistentId.toString());

        assertNull(result);
        verify(taskRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should get paginated tasks with default sorting")
    void shouldGetPaginatedTasksWithDefaultSorting() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Task> taskPage = new PageImpl<>(Arrays.asList(testTask));
        
        when(taskRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(taskPage);

        Page<Task> result = taskService.getTasks(
                null, null, null, null, null, 0, 10, false, "createdAt", "desc"
        );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testTask.getTitle(), result.getContent().get(0).getTitle());
        verify(taskRepository, times(1)).findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Should get paginated tasks with ascending sorting")
    void shouldGetPaginatedTasksWithAscendingSorting() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("dueDate").ascending());
        Page<Task> taskPage = new PageImpl<>(Arrays.asList(testTask));
        
        when(taskRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(taskPage);

        Page<Task> result = taskService.getTasks(
                null, null, null, null, null, 0, 5, false, "dueDate", "asc"
        );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(taskRepository, times(1)).findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Should get paginated tasks with filters (not implemented yet)")
    void shouldGetPaginatedTasksWithFilters() {
        Pageable pageable = PageRequest.of(1, 20, Sort.by("title").descending());
        Page<Task> taskPage = new PageImpl<>(Arrays.asList(testTask));
        
        when(taskRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(taskPage);

        Page<Task> result = taskService.getTasks(
                null, null, true, 
                LocalDateTime.now().minusDays(7), 
                LocalDateTime.now().plusDays(7), 
                1, 20, false, "title", "desc"
        );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(taskRepository, times(1)).findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Should return null for update task (not implemented)")
    void shouldReturnNullForUpdateTask() {
        Task result = taskService.updateTask(testId.toString(), testTask);
        
        assertNull(result);
        // Verify no repository interaction since it's not implemented
        verifyNoInteractions(taskRepository);
    }

    @Test
    @DisplayName("Should handle delete task gracefully (not implemented)")
    void shouldHandleDeleteTaskGracefully() {
        assertDoesNotThrow(() -> {
            taskService.deleteTask(testId.toString());
        });
        // Verify no repository interaction since it's not implemented
        verifyNoInteractions(taskRepository);
    }

    @Test
    @DisplayName("Should handle invalid UUID string in getTaskById")
    void shouldHandleInvalidUuidStringInGetTaskById() {
        Task result = taskService.getTaskById("invalid-uuid-string");
        
        assertNull(result);
        // Verify repository interaction was attempted but failed gracefully
        verify(taskRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should create multiple tasks")
    void shouldCreateMultipleTasks() {
        Task task1 = new Task("Task 1", "Description 1", LocalDateTime.now().plusDays(1));
        Task task2 = new Task("Task 2", "Description 2", LocalDateTime.now().plusDays(2));
        
        when(taskRepository.save(task1)).thenReturn(task1);
        when(taskRepository.save(task2)).thenReturn(task2);

        Task createdTask1 = taskService.createTask(task1);
        Task createdTask2 = taskService.createTask(task2);

        assertEquals("Task 1", createdTask1.getTitle());
        assertEquals("Task 2", createdTask2.getTitle());
        verify(taskRepository, times(2)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should handle empty task list")
    void shouldHandleEmptyTaskList() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList());

        List<Task> result = taskService.getAllTasks();

        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle empty paginated results")
    void shouldHandleEmptyPaginatedResults() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Task> emptyPage = new PageImpl<>(Arrays.asList());
        
        when(taskRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(emptyPage);

        Page<Task> result = taskService.getTasks(
                null, null, null, null, null, 0, 10, false, "createdAt", "desc"
        );

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(taskRepository, times(1)).findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable));
    }
}
