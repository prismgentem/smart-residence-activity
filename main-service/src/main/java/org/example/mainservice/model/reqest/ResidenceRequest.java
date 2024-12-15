package org.example.mainservice.model.reqest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mainservice.model.response.ResidenceNewsResponse;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidenceRequest {
    private String name;
    private String address;
    private Double lon;
    private Double lat;
    private String managementCompany;
    private List<ResidenceNewsResponse> newsList;
}
