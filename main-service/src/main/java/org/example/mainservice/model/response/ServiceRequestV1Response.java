package org.example.mainservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mainservice.enums.ServiceCategory;
import org.example.mainservice.enums.ServiceType;
import org.example.mainservice.enums.Status;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestV1Response {
    private UUID id;
    private ShortUserResponse user;
    private ShortResidenceResponse residence;
    private ServiceType serviceType;
    private ServiceCategory serviceCategory;
    private Status status;
    private String description;
}
