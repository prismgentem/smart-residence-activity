package org.example.mainservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String secondName;
    private String email;
    private String phoneNumber;
    private ResidenceResponse residence;
    private List<ResidenceNewsResponse> residenceList;
}
