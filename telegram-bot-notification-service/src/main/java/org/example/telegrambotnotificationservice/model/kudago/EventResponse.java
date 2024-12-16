package org.example.telegrambotnotificationservice.model.kudago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private String title;
    private String description;
    private String ageRestriction;
    private String price;
    private List<EventDateResponse> dates;
    private List<String> images;
    private EventPlaceResponse place;
}
