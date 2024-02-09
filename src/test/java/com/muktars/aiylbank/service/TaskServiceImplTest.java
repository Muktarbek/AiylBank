package com.muktars.aiylbank.service;

import com.muktars.aiylbank.db.entity.Task;
import com.muktars.aiylbank.db.repository.TaskRepository;
import com.muktars.aiylbank.db.service.TaskServiceImpl;
import com.muktars.aiylbank.exception.InvalidDataException;
import com.muktars.aiylbank.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void getAllTasks() {
        Task task1 = new Task(1L, "Task 1", false);
        Task task2 = new Task(2L, "Task 2", true);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals(task1, result.get(0));
        assertEquals(task2, result.get(1));
    }

    @Test
    void getTaskById_ExistingId_ReturnsTask() {
        Task task = new Task(1L, "Task 1", false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertEquals(task, result);
    }

    @Test
    void getTaskById_NonExistingId_ThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void createTask_ValidData_ReturnsCreatedTask() {
        Task task = new Task(1L, "Task 1", false);
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        assertEquals(task, result);
    }

    @Test
    void createTask_EmptyDescription_ThrowsException() {
        Task task = new Task(1L, "", false);

        assertThrows(InvalidDataException.class, () -> taskService.createTask(task));
    }

    @Test
    void updateTask_ExistingId_ReturnsUpdatedTask() {
        Task existingTask = new Task(1L, "Task 1", false);
        Task updatedTaskDetails = new Task(1L, "Updated Task", true);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTaskDetails);

        assertEquals(updatedTaskDetails.getDescription(), result.getDescription());
        assertEquals(updatedTaskDetails.isCompleted(), result.isCompleted());
    }

    @Test
    void updateTask_NonExistingId_ThrowsException() {
        Task updatedTaskDetails = new Task(1L, "Updated Task", true);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(1L, updatedTaskDetails));
    }

    @Test
    void deleteTask_ExistingId_DeletesTask() {
        Task task = new Task(1L, "Task 1", false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void deleteTask_NonExistingId_ThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L));
    }
}

