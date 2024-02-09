package com.muktars.aiylbank.db.service;

import com.muktars.aiylbank.db.entity.Task;
import com.muktars.aiylbank.db.repository.TaskRepository;
import com.muktars.aiylbank.exception.InvalidDataException;
import com.muktars.aiylbank.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    @Override
    public Task createTask(Task task) {
        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            throw new InvalidDataException("Task description cannot be empty");
        }
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task taskDetails) {
        if (taskDetails.getDescription() == null || taskDetails.getDescription().isEmpty()) {
            throw new InvalidDataException("Task description cannot be empty");
        }
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
    }
}

