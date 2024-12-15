package org.example.mainservice.model.keycloak;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class KeycloakUser {
    private final String firstName;
    private final String lastName;
    private final String email;
}
