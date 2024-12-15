package org.example.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mainservice.controller.ServiceRequestController;
import org.example.mainservice.enums.ServiceCategory;
import org.example.mainservice.model.reqest.ServiceRequestV1Request;
import org.example.mainservice.model.response.ServiceRequestV1Response;
import org.example.mainservice.service.ServiceRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ServiceRequestControllerTest {

    private static final String SERVICE_REQUEST_URL = "/api/service-request";
    @Mock
    private ServiceRequestService serviceRequestService;
    @InjectMocks
    private ServiceRequestController serviceRequestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(serviceRequestController).build();
    }

    @Test
    void shouldCreateServiceRequestSuccessfully() throws Exception {
        // Arrange
        ServiceRequestV1Request request = buildServiceRequest("Test service request", ServiceCategory.HEATING_WATER_AND_SEWAGE);
        ServiceRequestV1Response response = buildServiceRequestResponse(request);

        when(serviceRequestService.createServiceRequest(any(ServiceRequestV1Request.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post(SERVICE_REQUEST_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test service request"))
                .andExpect(jsonPath("$.serviceCategory").value(ServiceCategory.HEATING_WATER_AND_SEWAGE.toString()));

        verify(serviceRequestService, times(1)).createServiceRequest(any(ServiceRequestV1Request.class));
    }

    @Test
    void shouldGetServiceRequestByIdSuccessfully() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        ServiceRequestV1Response response = buildServiceRequestResponse(id, "Test service request", ServiceCategory.DRY_CLEANING);

        when(serviceRequestService.getServiceRequestById(id)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get(SERVICE_REQUEST_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test service request"))
                .andExpect(jsonPath("$.serviceCategory").value(ServiceCategory.DRY_CLEANING.toString()));

        verify(serviceRequestService, times(1)).getServiceRequestById(id);
    }

    @Test
    void shouldGetAllServiceRequestsSuccessfully() throws Exception {
        // Arrange
        ServiceRequestV1Response response1 = buildServiceRequestResponse(UUID.randomUUID(), "Test service request 1", ServiceCategory.ELECTRICITY);
        ServiceRequestV1Response response2 = buildServiceRequestResponse(UUID.randomUUID(), "Test service request 2", ServiceCategory.CLEANING);

        when(serviceRequestService.getAllServiceRequest()).thenReturn(List.of(response1, response2));

        // Act & Assert
        mockMvc.perform(get(SERVICE_REQUEST_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Test service request 1"))
                .andExpect(jsonPath("$[1].description").value("Test service request 2"));

        verify(serviceRequestService, times(1)).getAllServiceRequest();
    }

    @Test
    void shouldUpdateServiceRequestSuccessfully() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        ServiceRequestV1Request request = buildServiceRequest("Updated service request", ServiceCategory.CLEANING);
        ServiceRequestV1Response response = buildServiceRequestResponse(id, "Updated service request", ServiceCategory.CLEANING);

        when(serviceRequestService.updateServiceRequest(eq(id), any(ServiceRequestV1Request.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put(SERVICE_REQUEST_URL + "/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated service request"))
                .andExpect(jsonPath("$.serviceCategory").value(ServiceCategory.CLEANING.toString()));

        verify(serviceRequestService, times(1)).updateServiceRequest(eq(id), any(ServiceRequestV1Request.class));
    }

    @Test
    void shouldDeleteServiceRequestSuccessfully() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(delete(SERVICE_REQUEST_URL + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(serviceRequestService, times(1)).deleteServiceRequest(id);
    }

    private ServiceRequestV1Request buildServiceRequest(String description, ServiceCategory serviceCategory) {
        ServiceRequestV1Request request = new ServiceRequestV1Request();
        request.setDescription(description);
        request.setServiceCategory(serviceCategory);
        return request;
    }

    private ServiceRequestV1Response buildServiceRequestResponse(ServiceRequestV1Request request) {
        return ServiceRequestV1Response.builder()
                .id(UUID.randomUUID())
                .description(request.getDescription())
                .serviceCategory(request.getServiceCategory())
                .serviceCategory(request.getServiceCategory())
                .build();
    }

    private ServiceRequestV1Response buildServiceRequestResponse(UUID id, String description, ServiceCategory serviceCategory) {
        return ServiceRequestV1Response.builder()
                .id(id)
                .description(description)
                .serviceCategory(serviceCategory)
                .build();
    }
}
