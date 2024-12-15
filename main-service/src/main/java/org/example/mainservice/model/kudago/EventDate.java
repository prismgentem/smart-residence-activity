package org.example.mainservice.model.kudago;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDate {
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("start_time")
    private LocalTime startTime;
    @JsonProperty("end_date")
    private LocalDate endDate;
    @JsonProperty("end_time")
    private LocalTime endTime;
}

