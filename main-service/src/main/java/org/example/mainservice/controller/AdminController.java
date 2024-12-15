package org.example.mainservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.mainservice.model.reqest.AdminRequest;
import org.example.mainservice.model.response.AdminResponse;
import org.example.mainservice.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
@SecurityRequirement(name = "Keycloak")
public class AdminController {
    private final AdminService adminService;
    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@RequestBody AdminRequest admin) {
        return ResponseEntity.ok(adminService.createAdmin(admin));
    }
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }
    @GetMapping
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }
    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable UUID id, @RequestBody AdminRequest admin) {
        return ResponseEntity.ok(adminService.updateAdmin(id, admin));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
