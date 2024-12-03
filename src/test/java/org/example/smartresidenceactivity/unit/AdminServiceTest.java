package org.example.smartresidenceactivity.unit;

import org.example.smartresidenceactivity.entity.Admin;
import org.example.smartresidenceactivity.entity.Residence;
import org.example.smartresidenceactivity.exception.ErrorType;
import org.example.smartresidenceactivity.exception.ServiceException;
import org.example.smartresidenceactivity.mapper.AdminRequestToAdminMapper;
import org.example.smartresidenceactivity.model.reqest.AdminRequest;
import org.example.smartresidenceactivity.model.response.AdminResponse;
import org.example.smartresidenceactivity.repository.AdminRepository;
import org.example.smartresidenceactivity.service.AdminService;
import org.example.smartresidenceactivity.service.JwtService;
import org.example.smartresidenceactivity.service.ValidationService;
import org.example.smartresidenceactivity.util.ErrorMessageConstants;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private ValidationService validationService;
    @Mock
    private JwtService jwtService;
    @Mock
    private ConversionService conversionService;
    @Mock
    private AdminRequestToAdminMapper adminRequestToAdminMapper;
    @InjectMocks
    private AdminService adminService;

    private EasyRandom easyRandom;
    private UUID testAdminId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        easyRandom = new EasyRandom();
        testAdminId = UUID.randomUUID();

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
    }

    @Test
    void getAdminByIdSuccess() {
        Admin admin = easyRandom.nextObject(Admin.class);
        admin.setId(testAdminId);

        doNothing().when(validationService).validateForAdminService(testAdminId);

        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(admin));
        AdminResponse expectedResponse = easyRandom.nextObject(AdminResponse.class);
        when(conversionService.convert(admin, AdminResponse.class)).thenReturn(expectedResponse);

        AdminResponse actualResponse = adminService.getAdminById(testAdminId);

        assertEquals(expectedResponse, actualResponse);
        verify(adminRepository).findById(testAdminId);
        verify(validationService).validateForAdminService(testAdminId);
    }

    @Test
    void getAdminByIdNotFound() {
        doNothing().when(validationService).validateForAdminService(testAdminId);
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.empty());

        var thrown = assertThrows(ServiceException.class, () -> adminService.getAdminById(testAdminId));
        assertEquals(ErrorType.BAD_REQUEST.getCode(), thrown.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, testAdminId), thrown.getMessage());

        verify(adminRepository).findById(testAdminId);
        verify(validationService).validateForAdminService(testAdminId);
    }


    @Test
    void getAllAdminsSuccess() {
        var admin1 = easyRandom.nextObject(Admin.class);
        admin1.setId(UUID.randomUUID());
        admin1.setResidence(easyRandom.nextObject(Residence.class));
        var admin2 = easyRandom.nextObject(Admin.class);
        admin2.setId(UUID.randomUUID());
        admin2.setResidence(admin1.getResidence());

        var residence = admin1.getResidence();
        when(validationService.validateAndGet(any(UUID.class), anyList())).thenReturn(admin1);
        when(adminRepository.findAllByResidenceId(residence.getId())).thenReturn(List.of(admin1, admin2));

        var adminResponse1 = easyRandom.nextObject(AdminResponse.class);
        var adminResponse2 = easyRandom.nextObject(AdminResponse.class);
        when(conversionService.convert(admin1, AdminResponse.class)).thenReturn(adminResponse1);
        when(conversionService.convert(admin2, AdminResponse.class)).thenReturn(adminResponse2);

        List<AdminResponse> actualResponse = adminService.getAllAdmins();

        assertEquals(2, actualResponse.size());
        assertEquals(adminResponse1, actualResponse.get(0));
        assertEquals(adminResponse2, actualResponse.get(1));

        verify(adminRepository).findAllByResidenceId(residence.getId());
        verify(validationService).validateAndGet(any(UUID.class), anyList());
    }

    @Test
    void updateAdminSuccess() {
        Admin adminForUpdate = easyRandom.nextObject(Admin.class);
        adminForUpdate.setId(testAdminId);

        AdminRequest adminRequest = easyRandom.nextObject(AdminRequest.class);
        AdminResponse expectedResponse = easyRandom.nextObject(AdminResponse.class);

        doNothing().when(validationService).validateForAdminService(testAdminId);
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(adminForUpdate));

        when(adminRequestToAdminMapper.toAdminForUpdate(adminForUpdate, adminRequest)).thenReturn(adminForUpdate);

        when(adminRepository.save(adminForUpdate)).thenReturn(adminForUpdate);

        when(conversionService.convert(adminForUpdate, AdminResponse.class)).thenReturn(expectedResponse);

        AdminResponse actualResponse = adminService.updateAdmin(testAdminId, adminRequest);

        assertEquals(expectedResponse, actualResponse);

        verify(adminRepository).findById(testAdminId);
        verify(adminRequestToAdminMapper).toAdminForUpdate(adminForUpdate, adminRequest);
        verify(adminRepository).save(adminForUpdate);
        verify(conversionService).convert(adminForUpdate, AdminResponse.class);
    }

    @Test
    void updateAdminNotFound() {
        AdminRequest adminRequest = easyRandom.nextObject(AdminRequest.class);

        when(adminRepository.findById(testAdminId)).thenReturn(Optional.empty());

        ServiceException thrown = assertThrows(ServiceException.class, () -> adminService.updateAdmin(testAdminId, adminRequest));
        assertEquals(ErrorType.BAD_REQUEST.getCode(), thrown.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, testAdminId), thrown.getMessage());

        verify(adminRepository).findById(testAdminId);
        verify(validationService, never()).validateForAdminService(testAdminId);
    }

    @Test
    void deleteAdminSuccess() {
        Admin admin = easyRandom.nextObject(Admin.class);
        admin.setId(testAdminId);

        doNothing().when(validationService).validateForAdminService(testAdminId);
        doNothing().when(adminRepository).deleteById(testAdminId);

        adminService.deleteAdmin(testAdminId);

        verify(validationService).validateForAdminService(testAdminId);

        verify(adminRepository).deleteById(testAdminId);
    }

}
