package org.example.smartresidenceactivity.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventPlaceResponse {
    private Integer id;
    private String address;
    private String subway;
    private String title;
}