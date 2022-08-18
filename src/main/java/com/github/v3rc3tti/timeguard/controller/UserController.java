package com.github.v3rc3tti.timeguard.controller;

import com.github.v3rc3tti.timeguard.model.Task;
import com.github.v3rc3tti.timeguard.storage.TaskRepository;
import com.github.v3rc3tti.timeguard.storage.UserRepository;
import com.github.v3rc3tti.timeguard.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserController(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Long userId) {
        return userRepository.findOne(userId);
    }

    @PostMapping
    public Long save(@RequestBody User user) {
        return userRepository.save(user).getId();
    }

    @GetMapping("/{userId}/tasks")
    public Iterable<Task> getUserTasks(@PathVariable Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @PostMapping("/{userId}/tasks")
    public Long save(@PathVariable Long userId, @RequestBody @Valid Task task) {
        task.setUserId(userId);
        return taskRepository.save(task).getId();
    }

    @DeleteMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskRepository.deleteById(taskId);
        return ResponseEntity.ok().build();
    }
}
