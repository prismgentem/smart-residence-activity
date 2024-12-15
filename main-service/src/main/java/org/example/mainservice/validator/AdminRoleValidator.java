//package org.example.smartresidenceactivity.validator;
//
//import lombok.RequiredArgsConstructor;
//import org.example.smartresidenceactivity.entity.Admin;
//import org.example.smartresidenceactivity.exception.ErrorType;
//import org.example.smartresidenceactivity.exception.ServiceException;
//import org.example.smartresidenceactivity.repository.AdminRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//class AdminRoleValidator implements RoleValidator {
//    private final AdminRepository adminRepository;
//
//    @Override
//    public Admin validate(UUID userId) {
//        return adminRepository.findById(userId).orElseThrow(
//                () -> new ServiceException(ErrorType.NOT_FOUND, "Admin not found")
//        );
//    }
//
//    @Override
//    public String getRoleName() {
//        return "ROLE_ADMIN";
//    }
//}
