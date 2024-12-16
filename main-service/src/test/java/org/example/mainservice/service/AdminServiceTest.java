package org.example.mainservice.service;

import org.example.mainservice.entity.Admin;
import org.example.mainservice.entity.Residence;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.mapper.admin.AdminRequestToAdminMapper;
import org.example.mainservice.model.keycloak.KeycloakUser;
import org.example.mainservice.model.reqest.AdminRequest;
import org.example.mainservice.model.response.AdminResponse;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.service.AccessService;
import org.example.mainservice.service.AdminService;
import org.example.mainservice.service.JwtService;
import org.example.mainservice.service.KeycloakService;
import org.example.mainservice.util.ErrorMessageConstants;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private ConversionService conversionService;
    @Mock
    private AccessService accessService;
    @Mock
    private AdminRequestToAdminMapper adminRequestToAdminMapper;
    @Mock
    private KeycloakService keycloakService;
    @InjectMocks
    private AdminService adminService;

    private EasyRandom easyRandom;
    private UUID currentAdminId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        easyRandom = new EasyRandom();
        currentAdminId = UUID.randomUUID();

        when(jwtService.getCurrentUserId()).thenReturn(currentAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
    }

    @Test
    void createAdminSuccess() {
        // Arrange
        AdminRequest request = easyRandom.nextObject(AdminRequest.class);
        Admin admin = easyRandom.nextObject(Admin.class);
        KeycloakUser keycloakUser = easyRandom.nextObject(KeycloakUser.class);
        AdminResponse expectedResponse = easyRandom.nextObject(AdminResponse.class);

        when(adminRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(conversionService.convert(request, Admin.class)).thenReturn(admin);
        when(adminRepository.save(admin)).thenReturn(admin);
        when(conversionService.convert(admin, KeycloakUser.class)).thenReturn(keycloakUser);
        when(conversionService.convert(admin, AdminResponse.class)).thenReturn(expectedResponse);

        // Act
        AdminResponse actualResponse = adminService.createAdmin(request);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(adminRepository).findByEmail(request.getEmail());
        verify(conversionService).convert(request, Admin.class);
        verify(adminRepository).save(admin);
        verify(conversionService).convert(admin, KeycloakUser.class);
        verify(keycloakService).createUser(keycloakUser, "ROLE_ADMIN");
        verify(conversionService).convert(admin, AdminResponse.class);
    }

    @Test
    void createAdminEmailConflict() {
        // Arrange
        AdminRequest request = easyRandom.nextObject(AdminRequest.class);
        Admin existingAdmin = easyRandom.nextObject(Admin.class);

        when(adminRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingAdmin));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> adminService.createAdmin(request));

        assertEquals(ErrorType.CONFLICT.getCode(), exception.getCode());
    }

    @Test
    void getAdminByIdSuccess() {
        // Arrange
        Admin currentAdmin = easyRandom.nextObject(Admin.class);
        currentAdmin.setId(currentAdminId);
        Admin requestedAdmin = easyRandom.nextObject(Admin.class);

        currentAdmin.setResidence(new Residence());
        currentAdmin.getResidence().setAdmins(List.of(requestedAdmin));

        when(adminRepository.findById(currentAdminId)).thenReturn(Optional.of(currentAdmin));
        when(adminRepository.findById(requestedAdmin.getId())).thenReturn(Optional.of(requestedAdmin));
        AdminResponse expectedResponse = easyRandom.nextObject(AdminResponse.class);
        when(conversionService.convert(requestedAdmin, AdminResponse.class)).thenReturn(expectedResponse);

        // Act
        AdminResponse actualResponse = adminService.getAdminById(requestedAdmin.getId());

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(adminRepository).findById(currentAdminId);
        verify(adminRepository).findById(requestedAdmin.getId());
        verify(conversionService).convert(requestedAdmin, AdminResponse.class);
    }

    @Test
    void getAdminByIdNotFound() {
        when(adminRepository.findById(currentAdminId)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> adminService.getAdminById(currentAdminId));

        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, currentAdminId), exception.getMessage());

        verify(adminRepository).findById(currentAdminId);
    }

    @Test
    void getAdminByIdForbiddenAccess() {
        // Arrange
        Admin currentAdmin = easyRandom.nextObject(Admin.class);
        currentAdmin.setId(currentAdminId);
        Admin requestedAdmin = easyRandom.nextObject(Admin.class);

        currentAdmin.setResidence(new Residence());
        currentAdmin.getResidence().setAdmins(Collections.emptyList());

        when(adminRepository.findById(currentAdminId)).thenReturn(Optional.of(currentAdmin));
        when(adminRepository.findById(requestedAdmin.getId())).thenReturn(Optional.of(requestedAdmin));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> adminService.getAdminById(requestedAdmin.getId()));

        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
        assertEquals("Access denied: User is not an admin", exception.getMessage());

        verify(adminRepository).findById(currentAdminId);
        verify(adminRepository).findById(requestedAdmin.getId());
    }

    @Test
    void getAllAdminsSuccess() {
        // Arrange
        Admin currentAdmin = easyRandom.nextObject(Admin.class);
        currentAdmin.setId(currentAdminId);
        Residence residence = new Residence();
        residence.setId(UUID.randomUUID());
        currentAdmin.setResidence(residence);

        List<Admin> adminList = easyRandom.objects(Admin.class, 3).toList();

        when(adminRepository.findById(currentAdminId)).thenReturn(Optional.of(currentAdmin));
        when(adminRepository.findAllByResidenceId(residence.getId())).thenReturn(adminList);
        List<AdminResponse> expectedResponses = adminList.stream()
                .map(admin -> easyRandom.nextObject(AdminResponse.class))
                .toList();

        for (int i = 0; i < adminList.size(); i++) {
            when(conversionService.convert(adminList.get(i), AdminResponse.class)).thenReturn(expectedResponses.get(i));
        }

        // Act
        List<AdminResponse> actualResponses = adminService.getAllAdmins();

        // Assert
        assertEquals(expectedResponses, actualResponses);
        verify(adminRepository).findById(currentAdminId);
        verify(adminRepository).findAllByResidenceId(residence.getId());
    }

    @Test
    void getAllAdminsCurrentAdminNotFound() {
        // Arrange
        when(adminRepository.findById(currentAdminId)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                adminService::getAllAdmins);

        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, currentAdminId), exception.getMessage());

        verify(adminRepository).findById(currentAdminId);
    }


    @Test
    void updateAdminSuccess() {
        // Arrange
        UUID adminId = UUID.randomUUID();
        AdminRequest request = easyRandom.nextObject(AdminRequest.class);
        Admin adminForUpdate = easyRandom.nextObject(Admin.class);
        Admin updatedAdmin = easyRandom.nextObject(Admin.class);

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(adminForUpdate));
        when(adminRequestToAdminMapper.toAdminForUpdate(adminForUpdate, request)).thenReturn(updatedAdmin);
        when(adminRepository.save(updatedAdmin)).thenReturn(updatedAdmin);
        when(conversionService.convert(updatedAdmin, AdminResponse.class))
                .thenReturn(easyRandom.nextObject(AdminResponse.class));

        // Act
        AdminResponse actualResponse = adminService.updateAdmin(adminId, request);

        // Assert
        verify(accessService).checkAdminAccess(adminId);
        verify(keycloakService).updateUser(updatedAdmin.getEmail(), conversionService.convert(updatedAdmin, KeycloakUser.class));
        verify(adminRepository).findById(adminId);
        verify(adminRepository).save(updatedAdmin);
    }

    @Test
    void updateAdminNotFound() {
        // Arrange
        UUID adminId = UUID.randomUUID();
        AdminRequest request = easyRandom.nextObject(AdminRequest.class);

        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> adminService.updateAdmin(adminId, request));

        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, adminId), exception.getMessage());

        verify(adminRepository).findById(adminId);
    }

    @Test
    void deleteAdminSuccess() {
        // Arrange
        UUID adminId = UUID.randomUUID();
        Admin adminToDelete = easyRandom.nextObject(Admin.class);

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(adminToDelete));

        // Act
        adminService.deleteAdmin(adminId);

        // Assert
        verify(accessService).checkAdminAccess(adminId);
        verify(keycloakService).deleteUser(adminToDelete.getEmail());
        verify(adminRepository).deleteById(adminId);
    }

    @Test
    void deleteAdminNotFound() {
        // Arrange
        UUID adminId = UUID.randomUUID();

        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> adminService.deleteAdmin(adminId));

        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, adminId), exception.getMessage());

        verify(adminRepository).findById(adminId);
    }

}
