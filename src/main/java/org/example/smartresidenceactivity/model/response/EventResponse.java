package org.example.smartresidenceactivity.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.smartresidenceactivity.model.kudago.KudagoEventImage;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Integer id;
    private String title;
    private String description;
    private Integer ageRestriction;
    private String price;
    private List<EventDateResponse> dates;
    private List<KudagoEventImage> images;
    private EventPlaceResponse place;
}
