package org.example.mainservice.model.reqest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {
    private String firstName;
    private String lastName;
    private String secondName;
    private String email;
    private String phoneNumber;
    private UUID residenceId;
}
