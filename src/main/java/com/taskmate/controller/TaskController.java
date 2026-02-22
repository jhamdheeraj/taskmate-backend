package com.taskmate.controller;

import com.taskmate.entity.Priority;
import com.taskmate.entity.Task;
import com.taskmate.entity.TaskStatus;
import com.taskmate.pojos.TaskResponse;
import com.taskmate.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody Task task, org.springframework.validation.BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Task createdTask = taskService.createTask(task);
        TaskResponse taskResponse = new TaskResponse(createdTask, "Task create Successfully!", true);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }


    @GetMapping(value = "/{id}")
    public Task getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> softDeleteTask(@PathVariable String id) {
        boolean deleted = taskService.softDeleteTask(id);
        Map<String, Object> response = new HashMap<>();
        
        if (deleted) {
            response.put("success", true);
            response.put("message", "Task deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Task not found or invalid ID");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Boolean overdue,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") Boolean deleted,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Page<Task> tasks = taskService.getTasks(status, priority, overdue, dueFrom, dueTo, page, size,deleted, sortBy, direction);

        return ResponseEntity.ok(tasks);
    }


}
