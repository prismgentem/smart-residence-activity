package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.entity.Admin;
import org.example.smartresidenceactivity.entity.User;
import org.example.smartresidenceactivity.exception.ErrorType;
import org.example.smartresidenceactivity.exception.ServiceException;
import org.example.smartresidenceactivity.repository.AdminRepository;
import org.example.smartresidenceactivity.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;

    public Object validateAndGet(UUID userId, List<String> userRoles) {
        if (userRoles.contains("ROLE_ADMIN")) {
            return validateAdmin(userId);
        } else if (userRoles.contains("ROLE_USER")) {
            return validateUser(userId);
        } else {
            throw new ServiceException(ErrorType.FORBIDDEN, "No valid role found for validation");
        }
    }

    private Admin validateAdmin(UUID userId) {
        return adminRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorType.NOT_FOUND, "Admin not found")
        );
    }

    private User validateUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorType.NOT_FOUND, "User not found")
        );
    }

    public void validateProfileResidence(UUID residenceId) {
        var id = jwtService.getCurrentUserId();
        var roles = jwtService.getCurrentUserRoles();
        var profile = validateAndGet(id, roles);
        if (profile instanceof User user) {
            if (!(user.getResidence().getId().equals(residenceId))) {
                throw new ServiceException(ErrorType.FORBIDDEN, "User does not have access to this residence");
            }
        } else if (profile instanceof Admin admin) {
            if (!(admin.getResidence().getId().equals(residenceId))) {
                throw new ServiceException(ErrorType.FORBIDDEN, "Admin does not have access to this residence");
            }
        }
    }


    public void validateForUserService(UUID userId) {
        if (jwtService.getCurrentUserId().equals(userId)) {
            return;
        }

        var admin = adminRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorType.FORBIDDEN, "You do not have access to this user")
        );

        var user = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorType.FORBIDDEN, "You do not have access to this user")
        );

        if (!admin.getResidence().getId().equals(user.getResidence().getId())) {
            throw new ServiceException(ErrorType.FORBIDDEN, "You do not have access to this user");
        }
    }


    public void validateForAdminService(UUID id) {
        if (!(jwtService.getCurrentUserId().equals(id))) {
            throw new ServiceException(ErrorType.FORBIDDEN, "You do not have access to this admin");
        }
    }
}
