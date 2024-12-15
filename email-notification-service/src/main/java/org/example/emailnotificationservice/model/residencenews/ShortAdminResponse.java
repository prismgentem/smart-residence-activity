package org.example.emailnotificationservice.model.residencenews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortAdminResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String secondName;
    private String email;
}
