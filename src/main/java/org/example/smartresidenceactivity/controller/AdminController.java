package org.example.smartresidenceactivity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.AdminRequest;
import org.example.smartresidenceactivity.model.response.AdminResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Api(value = "Admin Controller", tags = {"Admin API"})
public class AdminController {
    @ApiOperation(value = "Создание администратора")
    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@RequestBody AdminRequest admin) {
        return null;
    }

    @ApiOperation(value = "Получение администратора по id")
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable UUID id) {
        return null;
    }

    @ApiOperation(value = "Получение всех администраторов")
    @GetMapping
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        return null;
    }

    @ApiOperation(value = "Обновление администратора")
    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable UUID id, @RequestBody AdminRequest admin) {
        return null;
    }

    @ApiOperation(value = "Удаление администратора по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id) {
        return null;
    }
}
