package org.example.emailnotificationservice.model.kudago;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
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
    public void setAgeRestriction(Object ageRestriction) {
        this.ageRestriction = ageRestriction == null ? null : ageRestriction.toString();
    }
}
