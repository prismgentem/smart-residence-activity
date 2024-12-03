package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.exception.ErrorType;
import org.example.smartresidenceactivity.exception.ServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    public String getCurrentUserEmail() {
        var jwt = getCurrentJwtToken();
        return jwt.getClaimAsString("email");
    }

    public UUID getCurrentUserId() {
        var jwt = getCurrentJwtToken();
        return UUID.fromString(jwt.getClaimAsString("id"));
    }

    public List<String> getCurrentUserRoles() {
        var jwt = getCurrentJwtToken();
        List<String> roles = jwt.getClaimAsStringList("sra_roles");

        return roles.stream()
                .filter(role -> role.startsWith("ROLE_"))
                .toList();
    }

    private Jwt getCurrentJwtToken() {
        try {
            return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException(ErrorType.BAD_REQUEST, "Не удалось получить токен");
        }
    }
}
