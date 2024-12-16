package org.example.mainservice.service;

import org.example.mainservice.entity.Admin;
import org.example.mainservice.entity.Residence;
import org.example.mainservice.entity.ServiceRequest;
import org.example.mainservice.entity.User;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.repository.UserRepository;
import org.example.mainservice.service.AccessService;
import org.example.mainservice.service.JwtService;
import org.example.mainservice.util.ErrorMessageConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccessServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AccessService accessService;

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
    void checkAccessResidenceAwareToUser_AdminAccess() {
        // Arrange
        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(testResidenceId);

        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(mockAdmin));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));

        // Act
        accessService.checkAccessResidenceAwareToUser(testUserId);

        // Assert
        verify(adminRepository).findById(testAdminId);
        verify(userRepository).findById(testUserId);
    }

    @Test
    void checkAccessResidenceAwareToUser_AdminAccessUserForbidden() {
        // Arrange
        var residenceForUser = Residence.builder()
                .id(UUID.fromString("30a69c1b-7efc-4c62-bcaa-eb366061f477"))
                .build();

        var residenceForAdmin = Residence.builder()
                .id(UUID.fromString("cfe543ae-5cb6-4044-8085-d3432f7da36e"))
                .build();

        var mockAdmin = new Admin();
        mockAdmin.setId(testAdminId);
        mockAdmin.setResidence(residenceForUser);

        var mockUser = new User();
        mockUser.setId(testUserId);
        mockUser.setResidence(residenceForAdmin);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(mockAdmin));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidenceAwareToUser(testUserId);
        });

        // Assert
        verify(adminRepository).findById(testAdminId);
        verify(userRepository).findById(testUserId);
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
    }

    @Test
    void checkAccessResidenceAwareToUser_AdminNotFound() {
        // Arrange
        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(testResidenceId);

        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.empty());

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidenceAwareToUser(testAdminId);
        });

        // Assert
        verify(adminRepository).findById(testAdminId);
        assertEquals(ErrorType.NOT_FOUND.getCode(), exception.getCode());
    }

    @Test
    void checkAccessResidenceAwareToUser_UserNotFound() {
        // Arrange
        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(testResidenceId);

        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(mockAdmin));
        when(adminRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidenceAwareToUser(testAdminId);
        });

        // Assert
        verify(adminRepository).findById(testAdminId);
        assertEquals(ErrorType.NOT_FOUND.getCode(), exception.getCode());
    }

    @Test
    void checkAccessResidenceAwareToUser_UserAccessDenied() {
        // Arrange
        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(testResidenceId);

        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(UUID.randomUUID());

        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidenceAwareToUser(testAdminId);
        });
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
        assertEquals("Access denied: User is not an admin", exception.getMessage());
    }

    @Test
    void checkAccessResidenceAwareToResidence_Success_User() {
        // Arrange
        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));

        // Act
        accessService.checkAccessResidenceAwareToResidence(testResidenceId);

        // Assert
        verify(userRepository).findById(testUserId);
    }

    @Test
    void checkAccessResidenceAwareToResidence_NotFound_User() {
        // Arrange
        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidenceAwareToResidence(testResidenceId);
        });

        // Assert
        verify(userRepository).findById(testUserId);
        assertEquals(ErrorType.NOT_FOUND.getCode(), exception.getCode());
    }

    @Test
    void checkAccessResidenceAwareToResidence_NotAuthorized_User() {
        // Arrange
        var mockUser = new User();
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(UUID.randomUUID());

        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidenceAwareToResidence(testResidenceId);
        });
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_FORBIDDEN, testResidenceId), exception.getMessage());
    }

    @Test
    void checkAccessResidenceAwareToResidence_Success_Admin() {
        // Arrange
        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(testResidenceId);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(mockAdmin));

        // Act
        accessService.checkAccessResidenceAwareToResidence(testResidenceId);

        // Assert
        verify(adminRepository).findById(testAdminId);
    }

    @Test
    void checkAccessResidenceAwareToResidence_NotFound_Admin() {
        // Arrange
        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(testResidenceId);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.empty());

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidenceAwareToResidence(testResidenceId);
        });

        // Assert
        verify(adminRepository).findById(testAdminId);
        assertEquals(ErrorType.NOT_FOUND.getCode(), exception.getCode());
    }

    @Test
    void checkAccessResidenceAwareToResidence_NotAuthorized_Admin() {
        // Arrange
        var mockAdmin = new Admin();
        mockAdmin.setResidence(new Residence());
        mockAdmin.getResidence().setId(UUID.randomUUID());

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(mockAdmin));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidenceAwareToResidence(testResidenceId);
        });
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
    }

    @Test
    void checkAccessResidentAwareToServiceRequest_Success_User() {
        // Arrange
        var mockUser = new User();
        mockUser.setId(testUserId);
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        var mockServiceRequest = new ServiceRequest();
        mockServiceRequest.setUser(mockUser);

        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));

        // Act
        accessService.checkAccessResidentAwareToServiceRequest(mockServiceRequest);

        // Assert
        verify(userRepository).findById(testUserId);
    }

    @Test
    void checkAccessResidentAwareToServiceRequest_Success_Admin() {
        // Arrange
        var residence = Residence.builder()
                .id(testResidenceId)
                .build();

        var mockAdmin = Admin.builder()
                .id(testAdminId)
                .residence(residence)
                .build();

        var mockServiceRequest = new ServiceRequest();
        mockServiceRequest.setResidence(residence);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(mockAdmin));

        // Act
        accessService.checkAccessResidentAwareToServiceRequest(mockServiceRequest);

        // Assert
        verify(adminRepository).findById(testAdminId);
    }

    @Test
    void checkAccessResidentAwareToServiceRequest_NotFound_Admin() {
        // Arrange
        var residence = Residence.builder()
                .id(testResidenceId)
                .build();

        var mockServiceRequest = new ServiceRequest();
        mockServiceRequest.setResidence(residence);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.empty());

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidentAwareToServiceRequest(mockServiceRequest);
        });

        assertEquals(ErrorType.NOT_FOUND.getCode(), exception.getCode());

        // Assert
        verify(adminRepository).findById(testAdminId);
    }

    @Test
    void checkAccessResidentAwareToServiceRequest_Forbidden_Admin() {
        // Arrange
        var residenceForAdmin = Residence.builder()
                .id(testResidenceId)
                .build();

        var residenceForServiceRequest = Residence.builder()
                .id(UUID.fromString("565e4305-32e7-4f9c-9096-14ee4b61eb76"))
                .build();

        var mockAdmin = Admin.builder()
                .id(testAdminId)
                .residence(residenceForAdmin)
                .build();

        var mockServiceRequest = new ServiceRequest();
        mockServiceRequest.setResidence(residenceForServiceRequest);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_ADMIN"));
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(mockAdmin));

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidentAwareToServiceRequest(mockServiceRequest);
        });
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());

        // Assert
        verify(adminRepository).findById(testAdminId);
    }

    @Test
    void checkAccessResidentAwareToServiceRequest_NotFound_User() {
        // Arrange
        var mockUser = new User();
        mockUser.setId(testUserId);
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        var mockServiceRequest = new ServiceRequest();
        mockServiceRequest.setUser(mockUser);

        when(jwtService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidentAwareToServiceRequest(mockServiceRequest);
        });
        assertEquals(ErrorType.NOT_FOUND.getCode(), exception.getCode());
    }

    @Test
    void checkAccessResidentAwareToServiceRequest_Forbidden_User() {
        // Arrange
        var mockUser = new User();
        mockUser.setId(testUserId);
        mockUser.setResidence(new Residence());
        mockUser.getResidence().setId(testResidenceId);

        var notValidUser = User.builder()
                .id(UUID.fromString("6344ae78-4972-4b6b-9684-f23ce77b6c7a"))
                .build();

        var mockServiceRequest = new ServiceRequest();
        mockServiceRequest.setUser(notValidUser);

        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAccessResidentAwareToServiceRequest(mockServiceRequest);
        });
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
    }

    @Test
    void checkAdminAccess_Success() {
        // Arrange
        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);

        // Act
        accessService.checkAdminAccess(testAdminId);

        // Assert
        verify(jwtService).getCurrentUserId();
    }

    @Test
    void checkAdminAccess_Failure() {
        // Arrange
        when(jwtService.getCurrentUserId()).thenReturn(UUID.randomUUID());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accessService.checkAdminAccess(testAdminId);
        });
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
        assertEquals("Access denied: User is not an admin", exception.getMessage());
    }
}

