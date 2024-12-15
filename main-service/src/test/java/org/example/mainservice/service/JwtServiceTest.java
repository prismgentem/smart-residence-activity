package org.example.mainservice.service;

import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);

        jwtService = new JwtService();
    }

    @Test
    void getCurrentUserEmail_ShouldReturnEmail() {
        // Arrange
        String expectedEmail = "test@test.com";
        when(jwt.getClaimAsString("email")).thenReturn(expectedEmail);

        // Act
        String actualEmail = jwtService.getCurrentUserEmail();

        // Assert
        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    void getCurrentUserId_ShouldReturnUserId() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        when(jwt.getClaimAsString("id")).thenReturn(userId);

        // Act
        UUID actualUserId = jwtService.getCurrentUserId();

        // Assert
        assertEquals(UUID.fromString(userId), actualUserId);
    }

    @Test
    void getCurrentUserRoles_ShouldReturnRoles() {
        // Arrange
        List<String> expectedRoles = List.of("ROLE_USER", "ROLE_ADMIN");
        when(jwt.getClaimAsStringList("sra_roles")).thenReturn(expectedRoles);

        // Act
        List<String> actualRoles = jwtService.getCurrentUserRoles();

        // Assert
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    void getCurrentJwtToken_ShouldThrowServiceException_WhenTokenIsInvalid() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, jwtService::getCurrentUserEmail);
        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
        assertEquals("Не удалось получить токен", exception.getMessage());
    }
}

