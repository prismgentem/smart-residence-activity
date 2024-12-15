package org.example.mainservice.service;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.config.properties.KeycloakProperties;
import org.example.mainservice.model.keycloak.KeycloakUser;
import org.example.mainservice.util.PasswordGenerator;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.example.mainservice.util.ErrorMessageConstants.MSG_CREATE_USER_ERROR;
import static org.example.mainservice.util.ErrorMessageConstants.MSG_USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final KeycloakProperties properties;
    private final NotificationService notificationService;
    private Keycloak keycloak;

    private static final Integer PASSWORD_LENGTH = 12;

    @PostConstruct
    public void init() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(properties.getServerUrl())
                .realm(properties.getRealm())
                .grantType(properties.getGrantType())
                .clientId(properties.getClientId())
                .username(properties.getAdminUsername())
                .password(properties.getAdminPassword())
                .build();
        log.info("Keycloak client initialized for realm '{}'", properties.getRealm());
    }

    public void createUser(KeycloakUser user, String role) {
        var userName = String.format("%s_%s", user.getLastName(), user.getFirstName());
        log.info("Creating user with username: '{}', email: '{}'", userName, user.getEmail());

        var userId = createKeycloakUser(user, userName);
        log.info("User created in Keycloak with ID: '{}'", userId);

        assignRoleToUser(userId, role);
        log.info("Role '{}' assigned to user '{}'", role, userId);

        var password = PasswordGenerator.generatePassword(PASSWORD_LENGTH);
        setPassword(userId, password);
        log.info("Password set for user '{}'", userId);
        notificationService.sendCredentialsEmailNotification(user.getEmail(), userName, password);
    }

    public void deleteUser(String email) {
        var userId = findUserIdByEmail(email);
        log.info("Deleting user with ID: '{}'", userId);
        keycloak.realm(properties.getRealm())
                .users()
                .get(userId)
                .remove();
        log.info("User with ID '{}' successfully deleted", userId);
    }

    public void updateUser(String email, KeycloakUser user) {
        var userId = findUserIdByEmail(email);
        log.info("Updating user with ID: '{}'", userId);

        var userForUpdate = new UserRepresentation();
        userForUpdate.setEmail(user.getEmail());
        userForUpdate.setFirstName(user.getFirstName());
        userForUpdate.setLastName(user.getLastName());

        try {
            keycloak.realm(properties.getRealm())
                    .users()
                    .get(userId)
                    .update(userForUpdate);
            log.info("User with ID '{}' successfully updated", userId);
        } catch (Exception e) {
            log.error("Failed to update user with ID '{}': {}", userId, e.getMessage());
            throw new ServiceException(ErrorType.BAD_REQUEST, MSG_USER_NOT_FOUND);
        }
    }

    private String createKeycloakUser(KeycloakUser request, String username) {
        var user = new UserRepresentation();
        user.setEmail(request.getEmail());
        user.setUsername(username);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmailVerified(true);
        user.setEnabled(true);

        var response = keycloak.realm(properties.getRealm()).users().create(user);
        if (response.getStatus() != 201) {
            log.error("Failed to create user in Keycloak: status {}", response.getStatus());
            throw new ServiceException(ErrorType.BAD_REQUEST, MSG_CREATE_USER_ERROR);
        }

        String userId = extractIdFromLocation(response);
        log.info("Extracted user ID from Keycloak response: '{}'", userId);
        return userId;
    }

    private void assignRoleToUser(String userId, String roleName) {
        log.info("Assigning role '{}' to user '{}'", roleName, userId);

        var role = keycloak.realm(properties.getRealm())
                .roles()
                .get(roleName)
                .toRepresentation();

        keycloak.realm(properties.getRealm())
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(role));

        log.info("Role '{}' successfully assigned to user '{}'", roleName, userId);
    }

    private void setPassword(String userId, String password) {
        log.info("Setting password for user '{}'", userId);

        var credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        credentials.setTemporary(false);

        try {
            keycloak.realm(properties.getRealm())
                    .users()
                    .get(userId)
                    .resetPassword(credentials);
            log.info("Password successfully set for user '{}'", userId);
        } catch (Exception e) {
            log.error("Failed to set password for user '{}': {}", userId, e.getMessage());
            throw new ServiceException(ErrorType.BAD_REQUEST, MSG_USER_NOT_FOUND);
        }
    }

    private String extractIdFromLocation(Response response) {
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        log.debug("Extracted user ID from response location: '{}'", userId);
        return userId;
    }

    private String findUserIdByEmail(String email) {
        List<UserRepresentation> users = keycloak.realm(properties.getRealm())
                .users()
                .search(email, 0, 1);
        if (users.isEmpty()) {
            log.error("User with email '{}' not found", email);
            throw new ServiceException(ErrorType.NOT_FOUND, MSG_USER_NOT_FOUND);
        }
        return users.get(0).getId();
    }
}
