package org.example.smartresidenceactivity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.AdminRequest;
import org.example.smartresidenceactivity.model.response.AdminResponse;
import org.example.smartresidenceactivity.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Api(value = "Admin Controller", tags = {"Admin API"})
public class AdminController {
    private final AdminService adminService;
    @ApiOperation(value = "Создание администратора")
    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@RequestBody AdminRequest admin) {
        return ResponseEntity.ok(adminService.createAdmin(admin));
    }

    @ApiOperation(value = "Получение администратора по id")
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @ApiOperation(value = "Получение всех администраторов")
    @GetMapping
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @ApiOperation(value = "Обновление администратора")
    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable UUID id, @RequestBody AdminRequest admin) {
        return ResponseEntity.ok(adminService.updateAdmin(id, admin));
    }

    @ApiOperation(value = "Удаление администратора по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
