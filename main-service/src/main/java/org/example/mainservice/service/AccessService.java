package org.example.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.repository.UserRepository;
import org.example.mainservice.entity.ServiceRequest;
import org.example.mainservice.util.ErrorMessageConstants;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public void checkAccessResidenceAwareToUser(UUID targetUserId) {
        var currentUserEmail = jwtService.getCurrentUserEmail();
        var currentUserRoles = jwtService.getCurrentUserRoles();

        if (currentUserRoles.contains("ROLE_USER")) {
            var currentUserId = userRepository.findByEmail(currentUserEmail).orElseThrow(
                    () -> new ServiceException(ErrorType.NOT_FOUND, "User not found")
            ).getId();
            if (!currentUserId.equals(targetUserId)) {
                throw new ServiceException(ErrorType.FORBIDDEN, "Access denied: User is not an admin");
            }
        } else if (currentUserRoles.contains("ROLE_ADMIN")) {
            var admin = adminRepository.findByEmail(currentUserEmail).orElseThrow(
                    () -> new ServiceException(ErrorType.NOT_FOUND, "Admin not found")
            );
            var targetUser = userRepository.findById(targetUserId).orElseThrow(
                    () -> new ServiceException(ErrorType.NOT_FOUND, "User not found"));

            if (!admin.getResidence().getId().equals(targetUser.getResidence().getId())) {
                throw new ServiceException(ErrorType.FORBIDDEN, "Access denied: User is not an admin");
            }
        }
    }

    public void checkAccessResidenceAwareToResidence(UUID residenceId) {
        var currentUserEmail = jwtService.getCurrentUserEmail();
        var currentUserRoles = jwtService.getCurrentUserRoles();

        if (currentUserRoles.contains("ROLE_USER")) {
            var user = userRepository.findByEmail(currentUserEmail).orElseThrow(
                    () -> new ServiceException(ErrorType.NOT_FOUND, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, currentUserEmail))
            );
            if (!user.getResidence().getId().equals(residenceId)) {
                throw new ServiceException(ErrorType.FORBIDDEN, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_FORBIDDEN, residenceId));
            }
        } else if (currentUserRoles.contains("ROLE_ADMIN")) {
            var admin = adminRepository.findByEmail(currentUserEmail).orElseThrow(
                    () -> new ServiceException(ErrorType.NOT_FOUND, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, currentUserEmail))
            );
            if (!admin.getResidence().getId().equals(residenceId)) {
                throw new ServiceException(ErrorType.FORBIDDEN, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_FORBIDDEN, residenceId));
            }
        }
    }

    public void checkAccessResidentAwareToServiceRequest(ServiceRequest serviceRequest) {
        var currentUserEmail = jwtService.getCurrentUserEmail();
        var currentUserRoles = jwtService.getCurrentUserRoles();

        if (currentUserRoles.contains("ROLE_USER")) {
            var user = userRepository.findByEmail(currentUserEmail).orElseThrow(
                    () -> new ServiceException(ErrorType.NOT_FOUND, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, currentUserEmail))
            );
            if (!user.getId().equals(serviceRequest.getUser().getId())) {
                throw new ServiceException(ErrorType.FORBIDDEN, ErrorMessageConstants.MSG_SERVICE_REQUEST_FORBIDDEN);
            }
        } else if (currentUserRoles.contains("ROLE_ADMIN")) {
            var admin = adminRepository.findByEmail(currentUserEmail).orElseThrow(
                    () -> new ServiceException(ErrorType.NOT_FOUND, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, currentUserEmail))
            );
            if (!admin.getResidence().getId().equals(serviceRequest.getResidence().getId())) {
                throw new ServiceException(ErrorType.FORBIDDEN, ErrorMessageConstants.MSG_SERVICE_REQUEST_FORBIDDEN);
            }
        }
    }

    public void checkAdminAccess(UUID id) {
        var currentUserId = jwtService.getCurrentUserId();
        if (!currentUserId.equals(id)) {
            throw new ServiceException(ErrorType.FORBIDDEN, "Access denied: User is not an admin");
        }
    }

}
