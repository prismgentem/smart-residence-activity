package org.example.mainservice.model.reqest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mainservice.enums.ServiceCategory;
import org.example.mainservice.enums.ServiceType;
import org.example.mainservice.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestV1Request {
    private ShortUserRequest user;
    private ShortResidenceRequest residence;
    private ServiceType serviceType;
    private ServiceCategory serviceCategory;
    private Status status;
    private String description;
}
