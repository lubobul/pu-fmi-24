package com.fmi.p0todo_app.controllers;

import com.fmi.p0todo_app.models.TaskModel;
import com.fmi.p0todo_app.services.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Value("${example.user}")
    private String userName;

    @GetMapping("/tasks")
    public List<TaskModel> getTasks(@RequestParam(required = false, defaultValue = "") String title) {
        return this.taskService.getTasks(title);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskModel> getTask(@PathVariable int taskId) {
        TaskModel task = this.taskService.getTask(taskId);

        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/tasks")
    public TaskModel createTask(@RequestBody TaskModel task) {
        return this.taskService.addTask(task);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<TaskModel> deleteTask(@PathVariable int taskId) {
        TaskModel task = this.taskService.deleteTask(taskId);

        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
