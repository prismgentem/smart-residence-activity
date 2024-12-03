package org.example.smartresidenceactivity.model.reqest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.smartresidenceactivity.model.response.AdminResponse;
import org.example.smartresidenceactivity.model.response.ResidenceNewsResponse;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidenceRequest {
    private UUID id;
    private String name;

    private String address;

    private String managementCompany;

    private List<AdminResponse> admins;

    private List<ResidenceNewsResponse> newsList;
}
