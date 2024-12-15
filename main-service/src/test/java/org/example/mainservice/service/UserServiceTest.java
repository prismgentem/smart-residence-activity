package org.example.mainservice.service;

import org.example.mainservice.entity.Admin;
import org.example.mainservice.entity.Residence;
import org.example.mainservice.entity.User;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.mapper.user.UserRequestToUserMapper;
import org.example.mainservice.model.keycloak.KeycloakUser;
import org.example.mainservice.model.reqest.UserRequest;
import org.example.mainservice.model.response.UserResponse;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.repository.UserRepository;
import org.example.mainservice.service.AccessService;
import org.example.mainservice.service.JwtService;
import org.example.mainservice.service.KeycloakService;
import org.example.mainservice.service.UserService;
import org.example.mainservice.util.ErrorMessageConstants;
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

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private ConversionService conversionService;

    @Mock
    private UserRequestToUserMapper userRequestToUserMapper;

    @Mock
    private AccessService accessService;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private UserService userService;

    private UUID testUserId;
    private UUID testAdminId;
    private UUID testResidenceId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUserId = UUID.randomUUID();
        testAdminId = UUID.randomUUID();
        testResidenceId = UUID.randomUUID();
    }

    @Test
    void createUserSuccess() {
        // Arrange
        var userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setFirstName("John");
        userRequest.setLastName("Doe");
        var user = new User();
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        var userResponse = new UserResponse();
        var keycloakUser = KeycloakUser.builder()
                .email(userRequest.getEmail())
                .lastName(userRequest.getLastName())
                .firstName(userRequest.getFirstName())
                .build();

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty()); // Нет такого пользователя
        when(conversionService.convert(userRequest, User.class)).thenReturn(user); // Преобразуем запрос в сущность User
        when(userRepository.save(user)).thenReturn(user); // Сохраняем пользователя
        when(conversionService.convert(user, KeycloakUser.class)).thenReturn(keycloakUser); // Преобразуем User в KeycloakUser
        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse); // Преобразуем User в UserResponse

        // Act
        var result = userService.createUser(userRequest);

        // Assert
        assertEquals(userResponse, result);
        verify(userRepository).save(user); // Проверка, что метод save был вызван
        verify(keycloakService).createUser(keycloakUser, "ROLE_USER"); // Проверка, что был вызван метод createUser у keycloakService
    }

    @Test
    void createUserEmailExists() {
        // Arrange
        var userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");

        // Act
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(new User()));

        // Assert
        var exception = assertThrows(ServiceException.class, () -> userService.createUser(userRequest));
        assertEquals(ErrorType.CONFLICT.getCode(), exception.getCode());
        assertEquals(ErrorMessageConstants.MSG_USER_EMAIL_EXISTS, exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(keycloakService, never()).createUser(any(KeycloakUser.class), eq("ROLE_USER"));
    }

    @Test
    void getUserByIdForUserSuccess() {
        // Arrange
        var user = new User();
        UserResponse userResponse = new UserResponse();
        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(user));
        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse);

        // Act
        var result = userService.getUserById(testUserId);

        // Assert
        assertEquals(userResponse, result);
        verify(accessService).checkAccessResidenceAwareToUser(testUserId);
        verify(userRepository).findById(testUserId);
    }

    @Test
    void getUserByIdForAdminSuccess() {
        // Arrange
        var user = new User();
        var userResponse = new UserResponse();
        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(user));
        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse);

        // Act
        var result = userService.getUserById(testUserId);

        // Assert
        assertEquals(userResponse, result);
        verify(accessService).checkAccessResidenceAwareToUser(testUserId);
        verify(userRepository).findById(testUserId);
    }

    @Test
    void getUserByIdNotFound() {
        // Arrange
        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(ServiceException.class, () -> userService.getUserById(testUserId));

        // Assert
        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
    }

    @Test
    void getAllUsersByIdForAdminSuccess() {
        // Arrange
        var user = new User();
        var userResponse = new UserResponse();

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);

        var mockAdmin = new Admin();
        var mockResidence = new Residence();
        mockResidence.setId(testResidenceId);
        mockAdmin.setResidence(mockResidence);

        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(mockAdmin));
        when(userRepository.findAllByResidenceId(testResidenceId)).thenReturn(List.of(user));
        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse);

        // Act
        var result = userService.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals(userResponse, result.get(0));
    }

    @Test
    void updateUserForAdminSuccess() {
        // Arrange
        var userRequest = new UserRequest();
        var user = new User();
        var userResponse = new UserResponse();
        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(user));
        when(userRequestToUserMapper.toUserForUpdate(user, userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse);

        // Act
        var result = userService.updateUser(testUserId, userRequest);

        // Assert
        assertEquals(userResponse, result);
        verify(accessService).checkAccessResidenceAwareToUser(testUserId);
        verify(userRepository).findById(testUserId);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserForUserSuccess() {
        // Arrange
        var userRequest = new UserRequest();
        var user = new User();
        var userResponse = new UserResponse();
        when(jwtService.getCurrentUserId()).thenReturn(testUserId);

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(user));
        when(userRequestToUserMapper.toUserForUpdate(user, userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse);

        // Act
        var result = userService.updateUser(testUserId, userRequest);

        // Assert
        assertEquals(userResponse, result);
        verify(accessService).checkAccessResidenceAwareToUser(testUserId);
        verify(userRepository).findById(testUserId);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserNotFound() {
        // Arrange
        var userRequest = new UserRequest();
        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(ServiceException.class, () -> userService.updateUser(testUserId, userRequest));

        // Assert
        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
    }

    @Test
    void deleteUserForAdminSuccess() {
        // Arrange
        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(new User()));

        // Act
        userService.deleteUser(testUserId);

        // Assert
        verify(accessService).checkAccessResidenceAwareToUser(testUserId);
        verify(userRepository).deleteById(testUserId);
    }

    @Test
    void deleteUserForUserSuccess() {
        // Arrange
        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(new User()));

        // Act
        userService.deleteUser(testUserId);

        // Assert
        verify(accessService).checkAccessResidenceAwareToUser(testUserId);
        verify(userRepository).deleteById(testUserId);
    }
}

