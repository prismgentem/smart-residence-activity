package org.example.mainservice.model.reqest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidenceNewsRequest {
    private String title;
    private String content;
    private Boolean sendNotification;
    private ShortResidenceRequest residence;
}
