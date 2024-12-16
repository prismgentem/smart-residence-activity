package org.example.mainservice.model.reqest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortResidenceRequest {
    private UUID id;
    private String name;
    private String address;
}
