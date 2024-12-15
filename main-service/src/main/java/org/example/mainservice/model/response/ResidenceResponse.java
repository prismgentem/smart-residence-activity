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
public class ResidenceResponse {
    private UUID id;
    private String name;
    private String address;
    private String managementCompany;
    private Double lon;
    private Double lat;
}
