package org.example.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mainservice.controller.ResidenceController;
import org.example.mainservice.model.reqest.ResidenceRequest;
import org.example.mainservice.model.response.ResidenceResponse;
import org.example.mainservice.service.ResidenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ResidenceControllerTest {

    @Mock
    private ResidenceService residenceService;

    @InjectMocks
    private ResidenceController residenceController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
    }

    @Test
    void shouldCreateResidenceSuccessfully() throws Exception {
        // Arrange
        ResidenceRequest residenceRequest = createResidenceRequest("Complex One", "Address 1");
        ResidenceResponse expectedResponse = createResidenceResponse(UUID.randomUUID(), "Complex One", "Address 1");

        when(residenceService.createResidence(residenceRequest)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/api/residence")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(residenceRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Complex One"))
                .andExpect(jsonPath("$.address").value("Address 1"));

        verify(residenceService, times(1)).createResidence(residenceRequest);
    }

    @Test
    void shouldReturnResidenceByIdSuccessfully() throws Exception {
        // Arrange
        UUID residenceId = UUID.randomUUID();
        ResidenceResponse expectedResponse = createResidenceResponse(residenceId, "Complex One", "Address 1");

        when(residenceService.getResidenceById(residenceId)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/residence/{id}", residenceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Complex One"))
                .andExpect(jsonPath("$.address").value("Address 1"));

        verify(residenceService, times(1)).getResidenceById(residenceId);
    }

    @Test
    void shouldReturnAllResidencesSuccessfully() throws Exception {
        // Arrange
        ResidenceResponse residence1 = createResidenceResponse(UUID.randomUUID(), "Complex One", "Address 1");
        ResidenceResponse residence2 = createResidenceResponse(UUID.randomUUID(), "Complex Two", "Address 2");

        when(residenceService.getAllResidences()).thenReturn(List.of(residence1, residence2));

        // Act & Assert
        mockMvc.perform(get("/api/residence"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Complex One"))
                .andExpect(jsonPath("$[1].name").value("Complex Two"));

        verify(residenceService, times(1)).getAllResidences();
    }

    @Test
    void shouldUpdateResidenceSuccessfully() throws Exception {
        // Arrange
        UUID residenceId = UUID.randomUUID();
        ResidenceRequest updatedResidenceRequest = createResidenceRequest("Updated Complex", "Updated Address");
        ResidenceResponse updatedResidenceResponse = createResidenceResponse(residenceId, "Updated Complex", "Updated Address");

        when(residenceService.updateResidence(residenceId, updatedResidenceRequest)).thenReturn(updatedResidenceResponse);

        // Act & Assert
        mockMvc.perform(put("/api/residence/{id}", residenceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(updatedResidenceRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Complex"))
                .andExpect(jsonPath("$.address").value("Updated Address"));

        verify(residenceService, times(1)).updateResidence(residenceId, updatedResidenceRequest);
    }

    @Test
    void shouldDeleteResidenceSuccessfully() throws Exception {
        // Arrange
        UUID residenceId = UUID.randomUUID();
        doNothing().when(residenceService).deleteResidence(residenceId);

        // Act & Assert
        mockMvc.perform(delete("/api/residence/{id}", residenceId))
                .andExpect(status().isNoContent());

        verify(residenceService, times(1)).deleteResidence(residenceId);
    }

    private ResidenceRequest createResidenceRequest(String name, String address) {
        return ResidenceRequest.builder()
                .name(name)
                .address(address)
                .build();
    }

    private ResidenceResponse createResidenceResponse(UUID id, String name, String address) {
        return ResidenceResponse.builder()
                .id(id)
                .name(name)
                .address(address)
                .build();
    }

    private String convertObjectToJson(Object object) throws Exception {
        return new ObjectMapper().writeValueAsString(object);
    }
}
