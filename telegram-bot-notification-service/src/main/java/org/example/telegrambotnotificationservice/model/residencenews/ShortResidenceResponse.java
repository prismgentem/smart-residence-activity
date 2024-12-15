package org.example.telegrambotnotificationservice.model.residencenews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortResidenceResponse {
    private UUID id;
    private String name;
    private String address;
}
