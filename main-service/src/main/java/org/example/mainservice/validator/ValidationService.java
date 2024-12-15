//package org.example.smartresidenceactivity.validator;
//
//import lombok.RequiredArgsConstructor;
//import org.example.smartresidenceactivity.entity.Admin;
//import org.example.smartresidenceactivity.entity.User;
//import org.example.smartresidenceactivity.exception.ErrorType;
//import org.example.smartresidenceactivity.exception.ServiceException;
//import org.example.smartresidenceactivity.repository.AdminRepository;
//import org.example.smartresidenceactivity.repository.UserRepository;
//import org.example.smartresidenceactivity.service.JwtService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ValidationService {
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//    private final AdminRepository adminRepository;
//    private final Map<String, RoleValidator> roleValidators;
//
//    public ValidationService(
//            List<RoleValidator> validators,
//            JwtService jwtService,
//            UserRepository userRepository,
//            AdminRepository adminRepository
//    ) {
//        this.jwtService = jwtService;
//        this.userRepository = userRepository;
//        this.adminRepository = adminRepository;
//        this.roleValidators = validators.stream()
//                .collect(Collectors.toMap(RoleValidator::getRoleName, validator -> validator));
//    }
//
//    public Object validateAndGet(UUID userId, List<String> userRoles) {
//        for (String role : userRoles) {
//            RoleValidator validator = roleValidators.get(role);
//            if (validator != null) {
//                return validator.validate(userId);
//            }
//        }
//        throw new ServiceException(ErrorType.FORBIDDEN, "No valid role found for validation");
//    }
//
//    public void validateProfileResidence(UUID residenceId) {
//        var currentUserId = jwtService.getCurrentUserId();
//        var currentRoles = jwtService.getCurrentUserRoles();
//        var profile = validateAndGet(currentUserId, currentRoles);
//
//        if (profile instanceof User user && !user.getResidence().getId().equals(residenceId)) {
//            throw new ServiceException(ErrorType.FORBIDDEN, "User does not have access to this residence");
//        }
//
//        if (profile instanceof Admin admin && !admin.getResidence().getId().equals(residenceId)) {
//            throw new ServiceException(ErrorType.FORBIDDEN, "Admin does not have access to this residence");
//        }
//    }
//
//    public void validateForUserService(UUID userId) {
//        if (jwtService.getCurrentUserId().equals(userId)) {
//            return;
//        }
//
//        var currentRoles = jwtService.getCurrentUserRoles();
//        var profile = validateAndGet(userId, currentRoles);
//
//        if (profile instanceof User user) {
//            if (!user.getResidence().getId().equals(getAdminResidenceId(userId))) {
//                throw new ServiceException(ErrorType.FORBIDDEN, "You do not have access to this user");
//            }
//        } else if (profile instanceof Admin admin) {
//            if (!admin.getResidence().getId().equals(getUserResidenceId(userId))) {
//                throw new ServiceException(ErrorType.FORBIDDEN, "You do not have access to this user");
//            }
//        }
//    }
//
//    public void validateForAdminService(UUID userId) {
//        if (!jwtService.getCurrentUserId().equals(userId)) {
//            throw new ServiceException(ErrorType.FORBIDDEN, "You do not have access to this admin");
//        }
//    }
//
//    private UUID getUserResidenceId(UUID userId) {
//        return userRepository.findById(userId).map(user -> user.getResidence().getId())
//                .orElseThrow(() -> new ServiceException(ErrorType.NOT_FOUND, "User not found"));
//    }
//
//    private UUID getAdminResidenceId(UUID adminId) {
//        return adminRepository.findById(adminId).map(admin -> admin.getResidence().getId())
//                .orElseThrow(() -> new ServiceException(ErrorType.NOT_FOUND, "Admin not found"));
//    }
//}
//
