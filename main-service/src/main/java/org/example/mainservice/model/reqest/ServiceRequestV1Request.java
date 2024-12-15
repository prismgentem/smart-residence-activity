package org.example.mainservice.model.reqest;

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
public class ServiceRequestV1Request {
    private UUID userId;
    private UUID residenceId;
    private ServiceType serviceType;
    private ServiceCategory serviceCategory;
    private String description;
}
