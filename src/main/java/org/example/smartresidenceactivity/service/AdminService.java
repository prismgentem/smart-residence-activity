package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.entity.Admin;
import org.example.smartresidenceactivity.entity.Residence;
import org.example.smartresidenceactivity.entity.User;
import org.example.smartresidenceactivity.exception.ErrorType;
import org.example.smartresidenceactivity.exception.ServiceException;
import org.example.smartresidenceactivity.mapper.AdminRequestToAdminMapper;
import org.example.smartresidenceactivity.model.reqest.AdminRequest;
import org.example.smartresidenceactivity.model.response.AdminResponse;
import org.example.smartresidenceactivity.repository.AdminRepository;
import org.example.smartresidenceactivity.util.ErrorMessageConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final ValidationService validationService;
    private final ConversionService conversionService;
    private final AdminRequestToAdminMapper adminRequestToAdminMapper;
    private final JwtService jwtService;

    @Transactional
    public AdminResponse createAdmin(AdminRequest request) {
        //TODO заводить админов из кейклока
        return null;
    }

    @Transactional(readOnly = true)
    public AdminResponse getAdminById(UUID id) {
        validationService.validateForAdminService(id);
        var admin = adminRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, id))
        );
        return conversionService.convert(admin, AdminResponse.class);
    }

    @Transactional(readOnly = true)
    public List<AdminResponse> getAllAdmins() {
        var profiel = validationService.validate(jwtService.getCurrentUserId(), jwtService.getCurrentUserRoles());
        Residence residence = null;
        if (profiel instanceof Admin) {
            residence = ((Admin) profiel).getResidence();
        } else if (profiel instanceof User) {
            residence = ((User) profiel).getResidence();
        } else {
            throw new ServiceException(ErrorType.FORBIDDEN, "No valid role found for validation");
        }
        var adminList = adminRepository.findAllByResidenceId(residence.getId());
        return adminList.stream().map(
                admin -> conversionService.convert(admin, AdminResponse.class)
        ).toList();
    }

    @Transactional
    public AdminResponse updateAdmin(UUID id, AdminRequest request) {
        var adminForUpdate = adminRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, id))
        );

        validationService.validateForAdminService(id);

        adminForUpdate = adminRequestToAdminMapper.toAdminForUpdate(adminForUpdate, request);
        return conversionService.convert(adminRepository.save(adminForUpdate), AdminResponse.class);
    }

    @Transactional
    public void deleteAdmin(UUID id) {
        validationService.validateForAdminService(id);
        adminRepository.deleteById(id);
    }
}
