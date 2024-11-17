package org.example.smartresidenceactivity.unit;

import org.example.smartresidenceactivity.entity.Admin;
import org.example.smartresidenceactivity.entity.User;
import org.example.smartresidenceactivity.entity.Residence;
import org.example.smartresidenceactivity.exception.ErrorType;
import org.example.smartresidenceactivity.exception.ServiceException;
import org.example.smartresidenceactivity.repository.AdminRepository;
import org.example.smartresidenceactivity.repository.UserRepository;
import org.example.smartresidenceactivity.service.JwtService;
import org.example.smartresidenceactivity.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ValidationService validationService;

    private UUID testUserId;
    private UUID testAdminId;
    private UUID testResidenceId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUserId = UUID.randomUUID();
        testAdminId = UUID.randomUUID();
        testResidenceId = UUID.randomUUID();
    }

    @Test
    void validateForUserService_UserIdMatches() {
        var testUserId = UUID.randomUUID();
        when(jwtService.getCurrentUserId()).thenReturn(testUserId);

        validationService.validateForUserService(testUserId);

        verify(jwtService).getCurrentUserId();
    }

    @Test
    void validateForUserService_UserIdDoesNotMatch_AdminAccess() {
        var testUserId = UUID.randomUUID();
        var currentUserId = UUID.randomUUID();

        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(testResidenceId);

        when(jwtService.getCurrentUserId()).thenReturn(currentUserId);
        when(adminRepository.findById(testUserId)).thenReturn(Optional.of(mockAdmin));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));

        validationService.validateForUserService(testUserId);

        verify(adminRepository).findById(testUserId);
        verify(userRepository).findById(testUserId);
    }

    @Test
    void validateForUserService_UserIdDoesNotMatch_AdminNoAccess() {
        var testUserId = UUID.randomUUID();
        var currentUserId = UUID.randomUUID();

        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(UUID.randomUUID());

        when(jwtService.getCurrentUserId()).thenReturn(currentUserId);
        when(adminRepository.findById(testUserId)).thenReturn(Optional.of(mockAdmin));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));

        var exception = assertThrows(ServiceException.class, () -> validationService.validateForUserService(testUserId));
        assertEquals("You do not have access to this user", exception.getMessage());
    }

    @Test
    void validateProfileResidence_Success_User() {
        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));
        when(userRepository.findById(testUserId)).thenReturn(java.util.Optional.of(mockUser));

        validationService.validateProfileResidence(testResidenceId);

        verify(userRepository).findById(testUserId);
    }

    @Test
    void validateUserProfileResidenceNotAuthorized() {
        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(UUID.randomUUID());

        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));
        when(userRepository.findById(testUserId)).thenReturn(java.util.Optional.of(mockUser));

        var exception = assertThrows(ServiceException.class, () -> validationService.validateProfileResidence(testResidenceId));
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
    }

    @Test
    void validateAdminProfileResidenceSuccess() {
        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(testResidenceId);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(java.util.Optional.of(mockAdmin));

        validationService.validateProfileResidence(testResidenceId);

        verify(adminRepository).findById(testAdminId);
    }

    @Test
    void validateAdminProfileResidenceNotAuthorized() {
        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(UUID.randomUUID());

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(java.util.Optional.of(mockAdmin));

        var exception = assertThrows(ServiceException.class, () -> validationService.validateProfileResidence(testResidenceId));
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
    }
}

