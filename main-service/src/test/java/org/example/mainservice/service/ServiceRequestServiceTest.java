package org.example.mainservice.service;

import org.example.mainservice.entity.Residence;
import org.example.mainservice.entity.ServiceRequest;
import org.example.mainservice.entity.User;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.mapper.servicerequest.ServiceRequestV1RequestToServiceRequestMapper;
import org.example.mainservice.model.reqest.ServiceRequestV1Request;
import org.example.mainservice.model.response.ServiceRequestV1Response;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.repository.ServiceRequestRepository;
import org.example.mainservice.repository.UserRepository;
import org.example.mainservice.service.AccessService;
import org.example.mainservice.service.JwtService;
import org.example.mainservice.service.ServiceRequestService;
import org.example.mainservice.util.ErrorMessageConstants;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceRequestServiceTest {

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private ConversionService conversionService;

    @Mock
    private ServiceRequestV1RequestToServiceRequestMapper serviceRequestV1RequestToServiceRequestMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AccessService accessService;

    @InjectMocks
    private ServiceRequestService serviceRequestService;

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        easyRandom = new EasyRandom();
    }

    @Test
    void createServiceRequest_Success() {
        var residence = easyRandom.nextObject(Residence.class);
        UUID userId = UUID.randomUUID();
        var request = easyRandom.nextObject(ServiceRequestV1Request.class);
        var user = easyRandom.nextObject(User.class);
        user.setId(userId);
        user.setResidence(residence);
        var serviceRequest = easyRandom.nextObject(ServiceRequest.class);
        serviceRequest.setResidence(residence);
        serviceRequest.setUser(user);
        var response = easyRandom.nextObject(ServiceRequestV1Response.class);

        when(jwtService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(conversionService.convert(request, ServiceRequest.class)).thenReturn(serviceRequest);
        when(serviceRequestRepository.save(serviceRequest)).thenReturn(serviceRequest);
        when(conversionService.convert(serviceRequest, ServiceRequestV1Response.class)).thenReturn(response);

        ServiceRequestV1Response result = serviceRequestService.createServiceRequest(request);

        assertNotNull(result);
        assertEquals(response, result);

        verify(jwtService).getCurrentUserId();
        verify(userRepository).findById(userId);
        verify(serviceRequestRepository).save(serviceRequest);
        verify(conversionService).convert(serviceRequest, ServiceRequestV1Response.class);
    }

    @Test
    void createServiceRequest_UserNotFound() {
        UUID userId = UUID.randomUUID();
        var request = easyRandom.nextObject(ServiceRequestV1Request.class);

        when(jwtService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> serviceRequestService.createServiceRequest(request));

        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, userId), exception.getMessage());

        verify(jwtService).getCurrentUserId();
        verify(userRepository).findById(userId);
    }

    @Test
    void getServiceRequestById_Success() {
        UUID requestId = UUID.randomUUID();
        var serviceRequest = easyRandom.nextObject(ServiceRequest.class);
        var response = easyRandom.nextObject(ServiceRequestV1Response.class);

        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(serviceRequest));
        when(conversionService.convert(serviceRequest, ServiceRequestV1Response.class)).thenReturn(response);

        ServiceRequestV1Response result = serviceRequestService.getServiceRequestById(requestId);

        assertNotNull(result);
        assertEquals(response, result);

        verify(accessService).checkAccessResidentAwareToServiceRequest(serviceRequest);
        verify(serviceRequestRepository).findById(requestId);
    }

    @Test
    void getServiceRequestById_NotFound() {
        UUID requestId = UUID.randomUUID();

        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> serviceRequestService.getServiceRequestById(requestId));

        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_SERVICE_REQUEST_NOT_FOUND, requestId), exception.getMessage());

        verify(serviceRequestRepository).findById(requestId);
    }

    @Test
    void getAllServiceRequest_AsUser() {
        UUID userId = UUID.randomUUID();
        var user = easyRandom.nextObject(User.class);
        user.setId(userId);
        var serviceRequest = easyRandom.nextObject(ServiceRequest.class);
        serviceRequest.setUser(user);
        var response = easyRandom.nextObject(ServiceRequestV1Response.class);

        when(jwtService.getCurrentUserId()).thenReturn(userId);
        when(jwtService.getCurrentUserRoles()).thenReturn(List.of("ROLE_USER"));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(serviceRequestRepository.findAllByUserId(userId)).thenReturn(List.of(serviceRequest));
        when(conversionService.convert(serviceRequest, ServiceRequestV1Response.class)).thenReturn(response);
        when(serviceRequestRepository.findAllByUserId(userId)).thenReturn(List.of(serviceRequest));
        when(conversionService.convert(serviceRequest, ServiceRequestV1Response.class)).thenReturn(response);

        List<ServiceRequestV1Response> result = serviceRequestService.getAllServiceRequest();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));

        verify(jwtService).getCurrentUserRoles();
        verify(serviceRequestRepository).findAllByUserId(userId);
    }

    @Test
    void updateServiceRequest_Success() {
        UUID requestId = UUID.randomUUID();
        var serviceRequest = easyRandom.nextObject(ServiceRequest.class);
        var request = easyRandom.nextObject(ServiceRequestV1Request.class);
        var updatedServiceRequest = easyRandom.nextObject(ServiceRequest.class);
        var response = easyRandom.nextObject(ServiceRequestV1Response.class);

        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(serviceRequest));
        doNothing().when(accessService).checkAccessResidentAwareToServiceRequest(serviceRequest);
        when(serviceRequestV1RequestToServiceRequestMapper.toServiceRequestForUpdate(serviceRequest, request))
                .thenReturn(updatedServiceRequest);
        when(serviceRequestRepository.save(updatedServiceRequest)).thenReturn(updatedServiceRequest);
        when(conversionService.convert(updatedServiceRequest, ServiceRequestV1Response.class)).thenReturn(response);

        ServiceRequestV1Response result = serviceRequestService.updateServiceRequest(requestId, request);

        assertNotNull(result);
        assertEquals(response, result);

        verify(serviceRequestRepository).findById(requestId);
        verify(accessService).checkAccessResidentAwareToServiceRequest(serviceRequest);
        verify(serviceRequestV1RequestToServiceRequestMapper).toServiceRequestForUpdate(serviceRequest, request);
        verify(serviceRequestRepository).save(updatedServiceRequest);
        verify(conversionService).convert(updatedServiceRequest, ServiceRequestV1Response.class);
    }

    @Test
    void updateServiceRequest_NotFound() {
        UUID requestId = UUID.randomUUID();
        var request = easyRandom.nextObject(ServiceRequestV1Request.class);

        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> serviceRequestService.updateServiceRequest(requestId, request));

        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_SERVICE_REQUEST_NOT_FOUND, requestId), exception.getMessage());

        verify(serviceRequestRepository).findById(requestId);
    }

    @Test
    void deleteServiceRequest_Success() {
        UUID requestId = UUID.randomUUID();
        var serviceRequest = easyRandom.nextObject(ServiceRequest.class);

        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(serviceRequest));
        doNothing().when(accessService).checkAccessResidentAwareToServiceRequest(serviceRequest);
        doNothing().when(serviceRequestRepository).deleteById(requestId);

        assertDoesNotThrow(() -> serviceRequestService.deleteServiceRequest(requestId));

        verify(serviceRequestRepository).findById(requestId);
        verify(accessService).checkAccessResidentAwareToServiceRequest(serviceRequest);
        verify(serviceRequestRepository).deleteById(requestId);
    }

    @Test
    void deleteServiceRequest_NotFound() {
        UUID requestId = UUID.randomUUID();

        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> serviceRequestService.deleteServiceRequest(requestId));

        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
        assertEquals(String.format(ErrorMessageConstants.MSG_SERVICE_REQUEST_NOT_FOUND, requestId), exception.getMessage());

        verify(serviceRequestRepository).findById(requestId);
    }

}
