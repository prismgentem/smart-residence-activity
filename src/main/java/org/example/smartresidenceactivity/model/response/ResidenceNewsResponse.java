package org.example.smartresidenceactivity.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidenceNewsResponse {
    private String title;

    private String content;

    private AdminResponse createdBy;

    private ResidenceResponse residence;
}
