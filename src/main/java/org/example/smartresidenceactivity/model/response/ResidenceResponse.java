package org.example.smartresidenceactivity.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidenceResponse {
    private String name;

    private String address;

    private String managementCompany;

    private List<AdminResponse> admins;

    private List<ResidenceNewsResponse> newsList;
}
