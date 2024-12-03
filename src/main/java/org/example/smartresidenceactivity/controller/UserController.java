package org.example.smartresidenceactivity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.UserRequest;
import org.example.smartresidenceactivity.model.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Api(value = "User Controller", tags = {"User API"})
public class UserController {
    @ApiOperation(value = "Создание пользователя")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest user) {
        return null;
    }

    @ApiOperation(value = "Получение пользователя по id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return null;
    }

    @ApiOperation(value = "Получение всех пользователей")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return null;
    }

    @ApiOperation(value = "Обновление пользователя")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody UserRequest user) {
        return null;
    }

    @ApiOperation(value = "Удаление пользователя по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        return null;
    }
}
