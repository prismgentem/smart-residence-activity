package org.example.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.repository.UserRepository;
import org.example.mainservice.entity.User;
import org.example.mainservice.mapper.user.UserRequestToUserMapper;
import org.example.mainservice.model.keycloak.KeycloakUser;
import org.example.mainservice.model.reqest.UserRequest;
import org.example.mainservice.model.response.UserResponse;
import org.example.mainservice.util.ErrorMessageConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ConversionService conversionService;
    private final UserRequestToUserMapper userRequestToUserMapper;
    private final KeycloakService keycloakService;
    private final AdminRepository adminRepository;
    private final AccessService accessService;
    private static final String ROLE_USER = "ROLE_USER";

    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (!userRepository.findByEmail(request.getEmail()).isEmpty())
            throw new ServiceException(ErrorType.CONFLICT, ErrorMessageConstants.MSG_USER_EMAIL_EXISTS);
        var user = userRepository.save(requireNonNull(conversionService.convert(request, User.class)));
        keycloakService.createUser(requireNonNull(conversionService.convert(user, KeycloakUser.class)), ROLE_USER);
        return conversionService.convert(user, UserResponse.class);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, id))
        );
        accessService.checkAccessResidenceAwareToUser(id);
        return conversionService.convert(user, UserResponse.class);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        var currentUserEmail = jwtService.getCurrentUserEmail();
        var admin = adminRepository.findByEmail(currentUserEmail).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, currentUserEmail))
        );
        var userList = userRepository.findAllByResidenceId(admin.getResidence().getId());
        return userList.stream()
                .map(userEntity -> conversionService.convert(userEntity, UserResponse.class))
                .toList();
    }

    @Transactional
    public UserResponse updateUser(UUID id, UserRequest user) {
        var userForUpdate = userRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, id))
        );
        accessService.checkAccessResidenceAwareToUser(id);
        userForUpdate = userRequestToUserMapper.toUserForUpdate(userForUpdate, user);
        keycloakService.updateUser(userForUpdate.getEmail(), conversionService.convert(userForUpdate, KeycloakUser.class));
        return conversionService.convert(userRepository.save(userForUpdate), UserResponse.class);
    }

    @Transactional
    public void deleteUser(UUID id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, id))
        );
        accessService.checkAccessResidenceAwareToUser(id);
        keycloakService.deleteUser(user.getEmail());
        userRepository.deleteById(id);
    }
}
