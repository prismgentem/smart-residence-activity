package org.example.mainservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidenceNewsResponse {
    private UUID id;
    private String title;
    private String content;
    private ShortAdminResponse createdBy;
    private Boolean sendNotification;
    private ShortResidenceResponse residence;
}
