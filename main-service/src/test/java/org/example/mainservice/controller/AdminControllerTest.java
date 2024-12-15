package org.example.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mainservice.controller.AdminController;
import org.example.mainservice.model.reqest.AdminRequest;
import org.example.mainservice.model.response.AdminResponse;
import org.example.mainservice.service.AdminService;
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

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void shouldCreateAdminSuccessfully() throws Exception {
        // Arrange
        AdminRequest adminRequest = createAdminRequest("John", "Doe", "john.doe@example.com");
        AdminResponse expectedResponse = createAdminResponse(UUID.randomUUID(), "John", "Doe", "john.doe@example.com");

        when(adminService.createAdmin(adminRequest)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/api/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(adminRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(adminService, times(1)).createAdmin(adminRequest);
    }

    @Test
    void shouldReturnAdminByIdSuccessfully() throws Exception {
        // Arrange
        UUID adminId = UUID.randomUUID();
        AdminResponse expectedResponse = createAdminResponse(adminId, "John", "Doe", "john.doe@example.com");

        when(adminService.getAdminById(adminId)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/admin/{id}", adminId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(adminService, times(1)).getAdminById(adminId);
    }

    @Test
    void shouldReturnAllAdminsSuccessfully() throws Exception {
        // Arrange
        AdminResponse admin1 = createAdminResponse(UUID.randomUUID(), "John", "Doe", "john.doe@example.com");
        AdminResponse admin2 = createAdminResponse(UUID.randomUUID(), "Jane", "Doe", "jane.doe@example.com");

        when(adminService.getAllAdmins()).thenReturn(List.of(admin1, admin2));

        // Act & Assert
        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(adminService, times(1)).getAllAdmins();
    }

    @Test
    void shouldUpdateAdminSuccessfully() throws Exception {
        // Arrange
        UUID adminId = UUID.randomUUID();
        AdminRequest updatedAdminRequest = createAdminRequest("John", "Doe", "john.doe@example.com");
        AdminResponse updatedAdminResponse = createAdminResponse(adminId, "John", "Doe", "john.doe@example.com");

        when(adminService.updateAdmin(adminId, updatedAdminRequest)).thenReturn(updatedAdminResponse);

        // Act & Assert
        mockMvc.perform(put("/api/admin/{id}", adminId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(updatedAdminRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(adminService, times(1)).updateAdmin(adminId, updatedAdminRequest);
    }

    @Test
    void shouldDeleteAdminSuccessfully() throws Exception {
        // Arrange
        UUID adminId = UUID.randomUUID();
        doNothing().when(adminService).deleteAdmin(adminId);

        // Act & Assert
        mockMvc.perform(delete("/api/admin/{id}", adminId))
                .andExpect(status().isNoContent());

        verify(adminService, times(1)).deleteAdmin(adminId);
    }

    // Helper methods to reduce redundancy and improve readability
    private AdminRequest createAdminRequest(String firstName, String lastName, String email) {
        return AdminRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }

    private AdminResponse createAdminResponse(UUID id, String firstName, String lastName, String email) {
        return AdminResponse.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }

    private String convertObjectToJson(Object object) throws Exception {
        return new ObjectMapper().writeValueAsString(object);
    }
}

