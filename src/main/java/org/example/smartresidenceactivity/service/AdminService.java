package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.AdminRequest;
import org.example.smartresidenceactivity.model.response.AdminResponse;
import org.example.smartresidenceactivity.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final ValidationService validationService;

    @Transactional
    public AdminResponse createAdmin(AdminRequest adminRequest) {
        return null;
    }

    @Transactional(readOnly = true)
    public AdminResponse getAdminById(UUID id) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<AdminResponse> getAllAdmins() {
        return null;
    }

    @Transactional
    public AdminResponse updateAdmin(UUID id, AdminRequest admin) {
        return null;
    }

    @Transactional
    public void deleteAdmin(UUID id) {
    }
}
