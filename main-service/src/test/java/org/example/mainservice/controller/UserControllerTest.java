package org.example.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mainservice.controller.UserController;
import org.example.mainservice.model.reqest.UserRequest;
import org.example.mainservice.model.response.UserResponse;
import org.example.mainservice.service.UserService;
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

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        // Arrange
        UserRequest userRequest = createUserRequest("John", "Doe", "john.doe@example.com");
        UserResponse expectedResponse = createUserResponse(UUID.randomUUID(), "John", "Doe", "john.doe@example.com");

        when(userService.createUser(userRequest)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).createUser(userRequest);
    }

    @Test
    void shouldReturnUserByIdSuccessfully() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserResponse expectedResponse = createUserResponse(userId, "John", "Doe", "john.doe@example.com");

        when(userService.getUserById(userId)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void shouldReturnAllUsersSuccessfully() throws Exception {
        // Arrange
        UserResponse user1 = createUserResponse(UUID.randomUUID(), "John", "Doe", "john.doe@example.com");
        UserResponse user2 = createUserResponse(UUID.randomUUID(), "Jane", "Doe", "jane.doe@example.com");

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        // Act & Assert
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        // Arrange
        var userId = UUID.randomUUID();
        var updateUserRequest = createUserRequest("John", "Doe", "john.doe@example.com");
        var updateUserResponse = createUserResponse(userId, "Jane", "Doe", "jane.doe@example.com");

        when(userService.updateUser(userId, updateUserRequest)).thenReturn(updateUserResponse);

        // Act & Assert
        mockMvc.perform(put("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"));
        // Verify
        verify(userService, times(1)).updateUser(userId, updateUserRequest);
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).deleteUser(userId);

        // Act & Assert
        mockMvc.perform(delete("/api/user/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

    private UserRequest createUserRequest(String firstName, String lastName, String email) {
        return UserRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }

    private UserResponse createUserResponse(UUID id, String firstName, String lastName, String email) {
        return UserResponse.builder()
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