package org.example.smartresidenceactivity.model.kudago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KudagoEvent {
    private Integer id;
    private String title;
    private KudagoPlace place;
    private String description;
    private Integer ageRestriction;
    private String price;
    private List<KudagoEventDate> dates;
    private List<KudagoEventImage> images;
}
