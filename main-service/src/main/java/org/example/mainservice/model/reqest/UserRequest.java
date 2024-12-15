package org.example.mainservice.model.reqest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String secondName;
    private String email;
    private String telegramUsername;
    private String phoneNumber;
    private Boolean verified;
    private ShortResidenceRequest residence;
}
