package org.example.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.enums.Status;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.mapper.servicerequest.ServiceRequestV1RequestToServiceRequestMapper;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.repository.ResidenceRepository;
import org.example.mainservice.repository.ServiceRequestRepository;
import org.example.mainservice.repository.UserRepository;
import org.example.mainservice.entity.ServiceRequest;
import org.example.mainservice.model.reqest.ServiceRequestV1Request;
import org.example.mainservice.model.response.ServiceRequestV1Response;
import org.example.mainservice.util.ErrorMessageConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class ServiceRequestService {
    private final ServiceRequestRepository serviceRequestRepository;
    private final ConversionService conversionService;
    private final ServiceRequestV1RequestToServiceRequestMapper serviceRequestV1RequestToServiceRequestMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final AccessService accessService;
    private final ResidenceRepository residenceRepository;

    @Transactional
    public ServiceRequestV1Response createServiceRequest(ServiceRequestV1Request request) {
        var currentUserEmail = jwtService.getCurrentUserEmail();
        var user = userRepository.findByEmail(currentUserEmail).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, currentUserEmail))
        );
        if (!user.getResidence().getId().equals(requireNonNull(request).getResidenceId())) {
            throw new ServiceException(ErrorType.FORBIDDEN, ErrorMessageConstants.MSG_SERVICE_REQUEST_FORBIDDEN);
        }
        var serviceRequest = conversionService.convert(request, ServiceRequest.class);
        serviceRequest.setUser(user);
        serviceRequest.setResidence(residenceRepository.findById(request.getResidenceId()).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_RESIDENCE_NOT_FOUND, request.getResidenceId()))
        ));
        serviceRequest.setStatus(Status.NEW);
        return conversionService.convert(serviceRequestRepository.save(requireNonNull(serviceRequest)), ServiceRequestV1Response.class);
    }

    @Transactional(readOnly = true)
    public ServiceRequestV1Response getServiceRequestById(UUID id) {
        var serviceRequest = serviceRequestRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_SERVICE_REQUEST_NOT_FOUND, id))
        );

        accessService.checkAccessResidentAwareToServiceRequest(serviceRequest);

        return conversionService.convert(serviceRequest, ServiceRequestV1Response.class);
    }

    @Transactional(readOnly = true)
    public List<ServiceRequestV1Response> getAllServiceRequest() {
        List<ServiceRequest> serviceRequestList = new ArrayList<>();
        var currentUserEmail = jwtService.getCurrentUserEmail();
        var currentUserRoles = jwtService.getCurrentUserRoles();

        if (currentUserRoles.contains("ROLE_USER")) {
            var user = userRepository.findByEmail(currentUserEmail).orElseThrow(
                    () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, currentUserEmail))
            );
            serviceRequestList = serviceRequestRepository.findAllByUserId(user.getId());
        } else if (currentUserRoles.contains("ROLE_ADMIN")) {
            var admin = adminRepository.findByEmail(currentUserEmail).orElseThrow(
                    () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, currentUserEmail))
            );
            serviceRequestList = serviceRequestRepository.findAllByResidenceId(admin.getResidence().getId());
        }

        return serviceRequestList.stream()
                .map(serviceRequest -> conversionService.convert(serviceRequest, ServiceRequestV1Response.class))
                .toList();
    }

    @Transactional
    public ServiceRequestV1Response updateServiceRequest(UUID id, ServiceRequestV1Request request) {
        var serviceRequestForUpdate = serviceRequestRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_SERVICE_REQUEST_NOT_FOUND, id)));

        accessService.checkAccessResidentAwareToServiceRequest(serviceRequestForUpdate);

        serviceRequestForUpdate = serviceRequestV1RequestToServiceRequestMapper.toServiceRequestForUpdate(serviceRequestForUpdate, request);
        return conversionService.convert(serviceRequestRepository.save(serviceRequestForUpdate), ServiceRequestV1Response.class);
    }

    @Transactional
    public void deleteServiceRequest(UUID id) {
        var serviceRequest = serviceRequestRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_SERVICE_REQUEST_NOT_FOUND, id))
        );

        accessService.checkAccessResidentAwareToServiceRequest(serviceRequest);
        serviceRequestRepository.deleteById(id);
    }
}
