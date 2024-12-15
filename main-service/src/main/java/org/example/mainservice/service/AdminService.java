package org.example.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.entity.Admin;
import org.example.mainservice.mapper.admin.AdminRequestToAdminMapper;
import org.example.mainservice.model.keycloak.KeycloakUser;
import org.example.mainservice.model.reqest.AdminRequest;
import org.example.mainservice.model.response.AdminResponse;
import org.example.mainservice.util.ErrorMessageConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final AccessService accessService;
    private final ConversionService conversionService;
    private final AdminRequestToAdminMapper adminRequestToAdminMapper;
    private final JwtService jwtService;
    private final KeycloakService keycloakService;
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Transactional
    public AdminResponse createAdmin(AdminRequest request) {
        if (!adminRepository.findByEmail(request.getEmail()).isEmpty()){
            throw new ServiceException(ErrorType.CONFLICT, ErrorMessageConstants.MSG_ADMIN_EMAIL_EXISTS);
        }
        var admin = adminRepository.save(requireNonNull(conversionService.convert(request, Admin.class)));
        keycloakService.createUser(requireNonNull(conversionService.convert(admin, KeycloakUser.class)), ROLE_ADMIN);
        return conversionService.convert(admin, AdminResponse.class);
    }

    @Transactional(readOnly = true)
    public AdminResponse getAdminById(UUID id) {
        var currentUserId = jwtService.getCurrentUserId();

        var currentAdmin = adminRepository.findById(currentUserId).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, id))
        );

        var admin = adminRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, id))
        );

        if (!currentAdmin.getResidence().getAdmins().contains(admin)) {
            throw new ServiceException(ErrorType.FORBIDDEN, "Access denied: User is not an admin");
        }

        return conversionService.convert(admin, AdminResponse.class);
    }

    @Transactional(readOnly = true)
    public List<AdminResponse> getAllAdmins() {
        var currentUserId = jwtService.getCurrentUserId();
        var admin = adminRepository.findById(currentUserId).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, currentUserId))
        );
        var adminList = adminRepository.findAllByResidenceId(admin.getResidence().getId());
        return adminList.stream().map(
                a -> conversionService.convert(a, AdminResponse.class)
        ).toList();
    }

    @Transactional
    public AdminResponse updateAdmin(UUID id, AdminRequest request) {
        accessService.checkAdminAccess(id);
        var adminForUpdate = adminRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, id))
        );
        adminForUpdate = adminRequestToAdminMapper.toAdminForUpdate(adminForUpdate, request);
        keycloakService.updateUser(adminForUpdate.getEmail(), conversionService.convert(adminForUpdate, KeycloakUser.class));
        return conversionService.convert(adminRepository.save(adminForUpdate), AdminResponse.class);
    }

    @Transactional
    public void deleteAdmin(UUID id) {
        accessService.checkAdminAccess(id);
        var admin = adminRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, id))
        );
        keycloakService.deleteUser(admin.getEmail());
        adminRepository.deleteById(id);
    }
}
