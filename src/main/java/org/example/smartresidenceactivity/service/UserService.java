package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.entity.Admin;
import org.example.smartresidenceactivity.exception.ErrorType;
import org.example.smartresidenceactivity.exception.ServiceException;
import org.example.smartresidenceactivity.mapper.UserRequestToUserMapper;
import org.example.smartresidenceactivity.model.reqest.UserRequest;
import org.example.smartresidenceactivity.model.response.UserResponse;
import org.example.smartresidenceactivity.repository.UserRepository;
import org.example.smartresidenceactivity.util.ErrorMessageConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ValidationService validationService;
    private final ConversionService conversionService;
    private final UserRequestToUserMapper userRequestToUserMapper;

    @Transactional
    public UserResponse createUser(UserRequest user) {
        //TODO заводить юзеров из кейклока
        return null;
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        validationService.validateForUserService(id);
        var user = userRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, id))
        );
        return conversionService.convert(user, UserResponse.class);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        var admin = validationService.validate(jwtService.getCurrentUserId(), jwtService.getCurrentUserRoles());
        if (!(admin instanceof Admin)) {
            throw new ServiceException(ErrorType.FORBIDDEN, "No valid role found for validation");
        }
        var residence = ((Admin) admin).getResidence();
        var userList = userRepository.findAllByResidenceId(residence.getId());
        return userList.stream().map(
                user -> conversionService.convert(user, UserResponse.class)
        ).toList();
    }

    @Transactional
    public UserResponse updateUser(UUID id, UserRequest user) {
        validationService.validateForUserService(id);
        var userForUpdate = userRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, id))
        );
        userForUpdate = userRequestToUserMapper.toUserForUpdate(userForUpdate, user);
        return conversionService.convert(userRepository.save(userForUpdate), UserResponse.class);
    }

    @Transactional
    public void deleteUser(UUID id) {
        validationService.validateForUserService(id);
        userRepository.deleteById(id);
    }
}
