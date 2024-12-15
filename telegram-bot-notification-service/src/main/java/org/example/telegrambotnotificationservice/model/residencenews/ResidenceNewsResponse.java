package org.example.telegrambotnotificationservice.model.residencenews;

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
    private ShortAdminResponse createdBy;
    private Boolean sendNotification;
    private ShortResidenceResponse residence;
}
