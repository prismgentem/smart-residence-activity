package org.example.mainservice.model.kudago;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private int id;
    private String title;
    private String description;
    private String ageRestriction;
    private String price;
    private List<EventDate> dates;
    private Place place;
    private List<Image> images;

    @JsonProperty("age_restriction")
    public void setAgeRestrictionFromJson(Object ageRestriction) {
        this.ageRestriction = ageRestriction == null ? null : ageRestriction.toString();
    }
}
