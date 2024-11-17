package org.example.smartresidenceactivity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.UserRequest;
import org.example.smartresidenceactivity.model.response.UserResponse;
import org.example.smartresidenceactivity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Api(value = "User Controller", tags = {"User API"})
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "Создание пользователя")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @ApiOperation(value = "Получение пользователя по id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @ApiOperation(value = "Получение всех пользователей")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @ApiOperation(value = "Обновление пользователя")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody UserRequest user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @ApiOperation(value = "Удаление пользователя по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
