package org.example.smartresidenceactivity.unit;

import org.example.smartresidenceactivity.entity.Admin;
import org.example.smartresidenceactivity.entity.Residence;
import org.example.smartresidenceactivity.entity.User;
import org.example.smartresidenceactivity.exception.ErrorType;
import org.example.smartresidenceactivity.exception.ServiceException;
import org.example.smartresidenceactivity.mapper.UserRequestToUserMapper;
import org.example.smartresidenceactivity.model.reqest.UserRequest;
import org.example.smartresidenceactivity.model.response.UserResponse;
import org.example.smartresidenceactivity.repository.UserRepository;
import org.example.smartresidenceactivity.service.JwtService;
import org.example.smartresidenceactivity.service.UserService;
import org.example.smartresidenceactivity.service.ValidationService;
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
    private ValidationService validationService;

    @Mock
    private ConversionService conversionService;

    @Mock
    private UserRequestToUserMapper userRequestToUserMapper;

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
    void getUserByIdForUserSuccess() {
        var user = new User();
        UserResponse userResponse = new UserResponse();
        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(user));
        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse);

        var result = userService.getUserById(testUserId);

        assertEquals(userResponse, result);
        verify(userRepository).findById(testUserId);
    }

    @Test
    void getUserByIdForAdminSuccess() {
        var user = new User();
        var userResponse = new UserResponse();
        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        doNothing().when(validationService).validateForAdminService(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(user));
        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse);

        var result = userService.getUserById(testUserId);

        assertEquals(userResponse, result);
        verify(userRepository).findById(testUserId);
    }

    @Test
    void getUserByIdNotFound() {
        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        var exception = assertThrows(ServiceException.class, () -> userService.getUserById(testUserId));
        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
    }

    @Test
    void getAllUsersByIdForAdminSuccess() {
        var user = new User();
        var userResponse = new UserResponse();

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);

        var mockAdmin = new Admin();
        var mockResidence = new Residence();
        mockResidence.setId(testResidenceId);
        mockAdmin.setResidence(mockResidence);
        when(validationService.validate(jwtService.getCurrentUserId(), jwtService.getCurrentUserRoles())).thenReturn(mockAdmin);

        when(userRepository.findAllByResidenceId(any(UUID.class))).thenReturn(List.of(user));

        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse);

        var result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(userResponse, result.get(0));
    }


    @Test
    void getAllUsersByIdForUserSuccess() {
        when(jwtService.getCurrentUserId()).thenReturn(testUserId);

        ServiceException exception = assertThrows(ServiceException.class, () -> userService.getAllUsers());
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
    }

    @Test
    void updateUserForAdminSuccess() {
        var userRequest = new UserRequest();
        var user = new User();
        var userResponse = new UserResponse();
        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        doNothing().when(validationService).validateForAdminService(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(user));
        when(userRequestToUserMapper.toUserForUpdate(user, userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse);

        var result = userService.updateUser(testUserId, userRequest);

        assertEquals(userResponse, result);
        verify(userRepository).findById(testUserId);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserForUserSuccess() {
        var userRequest = new UserRequest();
        var user = new User();
        var userResponse = new UserResponse();
        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        doNothing().when(validationService).validateForAdminService(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(user));
        when(userRequestToUserMapper.toUserForUpdate(user, userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(conversionService.convert(user, UserResponse.class)).thenReturn(userResponse);

        var result = userService.updateUser(testUserId, userRequest);

        assertEquals(userResponse, result);
        verify(userRepository).findById(testUserId);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserNotFound() {
        var userRequest = new UserRequest();
        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        doNothing().when(validationService).validateForAdminService(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        var exception = assertThrows(ServiceException.class, () -> userService.updateUser(testUserId, userRequest));
        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
    }

    @Test
    void deleteUserForAdminSuccess() {
        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        doNothing().when(validationService).validateForAdminService(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(new User()));

        userService.deleteUser(testUserId);

        verify(userRepository).deleteById(testUserId);
    }

    @Test
    void deleteUserForUserSuccess() {
        when(jwtService.getCurrentUserId()).thenReturn(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(new User()));

        userService.deleteUser(testUserId);

        verify(userRepository).deleteById(testUserId);
    }
}

