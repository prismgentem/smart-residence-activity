//package org.example.smartresidenceactivity.validator;
//
//import lombok.RequiredArgsConstructor;
//import org.example.smartresidenceactivity.entity.User;
//import org.example.smartresidenceactivity.exception.ErrorType;
//import org.example.smartresidenceactivity.exception.ServiceException;
//import org.example.smartresidenceactivity.repository.UserRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//class UserRoleValidator implements RoleValidator {
//    private final UserRepository userRepository;
//
//    @Override
//    public User validate(UUID userId) {
//        return userRepository.findById(userId).orElseThrow(
//                () -> new ServiceException(ErrorType.NOT_FOUND, "User not found")
//        );
//    }
//
//    @Override
//    public String getRoleName() {
//        return "ROLE_USER";
//    }
//}
