package org.example.smartresidenceactivity.model.reqest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.smartresidenceactivity.model.response.AdminResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidenceNewsRequest {
    private String title;

    private String content;

    private AdminResponse createdBy;

    private ResidenceRequest residence;
}
